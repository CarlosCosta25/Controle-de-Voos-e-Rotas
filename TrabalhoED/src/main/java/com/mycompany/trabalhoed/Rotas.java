package com.mycompany.trabalhoed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Rotas implements Cloneable {

    public String origem;
    public String destino;
    public double distancia;
    public List<Rotas> ListadeRotas;

    public Rotas[][] grafo;

    public Rotas() {
        grafo = new Rotas[23][23];
        ListadeRotas = new ArrayList<Rotas>();
    }

    private static int[] buscaPosicao(Arquivo aeroporto, String origem, String destino) {
        //funcao usada para encontrar os indices da matriz em no qual a aresta deve ser inserida
        int[] vet = new int[2];
        int ori, dest;
        for (ori = 0; ori < aeroporto.listadeAeroportos.size(); ori++) {// buscando o indice do aeroporto de origem
            if (aeroporto.listadeAeroportos.get(ori).aeroportos.equals(origem)) {
                break;
            }
        }
        vet[0] = ori;
        for (dest = 0; dest < aeroporto.listadeAeroportos.size(); dest++) {// buscando o indice do aeroporto de destino
            if (aeroporto.listadeAeroportos.get(dest).aeroportos.equals(destino)) {
                break;
            }
        }
        vet[1] = dest;
        return vet;
    }

    public void grafoRota(Arquivo aeroporto) {
        // O objetivo dessa função e estrair as informações das rotas e colocar na posição da matriz,
        //de modo que cada linha e coluna da matriz e de um determinado aeroporto. EX:  todos as rotas que patem ABQ está na linha 0
        // e todos as rotas que chegam em ABQ está na coluna 0.
        //a matriz e simetrica
        for (int i = 0; i < ListadeRotas.size(); i++) {
            int[] posicao = new int[2];
            posicao = buscaPosicao(aeroporto, ListadeRotas.get(i).origem, ListadeRotas.get(i).destino);
            Rotas simetrico = new Rotas();
            try {
                ListadeRotas.get(i).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            // clonando o objeto e invertendo os aeroportos para deixar a matriz simetrica
            simetrico.origem = ListadeRotas.get(i).destino;
            simetrico.destino = ListadeRotas.get(i).origem;
            simetrico.distancia = ListadeRotas.get(i).distancia;

            /* simetrico.qtdVoo = auxDeRotas.qtdVoo; */
            grafo[posicao[0]][posicao[1]] = ListadeRotas.get(i);
            grafo[posicao[1]][posicao[0]] = simetrico;

        }
    }

    public void Caminho(String aeroportoOrigem, String aeroportoDestino, Arquivo aeroporto) {
        // através da busca em profundidade eu verifico se possui um caminho entre os aeroportos
        Map<String, Boolean> visitados = new HashMap<String, Boolean>();
        List<String> print = new ArrayList<String>();
        for (int i = 0; i < grafo.length; i++) {// inicializando todos os aeroportos como não visitados
            visitados.put(aeroporto.listadeAeroportos.get(i).aeroportos, false);

        }
        visitados.replace(aeroportoOrigem, true);// mudando o origem para visitado

        boolean result = CaminhoRecursivo(aeroportoOrigem, aeroportoDestino, visitados, print);
        if (!result) {
            System.out.println("Não existe um caminho entre esses aeroportos");
        }
        for (int i = print.size() - 1; i >= 0; i--) {
            System.out.println(print.get(i));
        }
    }

    private boolean CaminhoRecursivo(String aeroportoOrigem, String aeroportoDestino, Map<String, Boolean> visitados, List print) {
        //essa função e responsavel por verificar todas as adjacencias do vertice e marcar como já visitados, isso acontece através da recursao
        int retorno;
        int posicao = 0;
        boolean achou = false;
        if (aeroportoOrigem.equals(aeroportoDestino)) {
            print.add(aeroportoDestino);
            // controle de retorno da recursão após encontrar o eroporto
            return true;
        }
        while (posicao < grafo.length) {
            if (achou == true) {
                break;
            }
            for (int j = 0; j < grafo.length; j++) {// busca o indice do aeroporto de origem no grafo
                if (grafo[posicao][j] != null && grafo[posicao][j].origem.equals(aeroportoOrigem)) {
                    achou = true;
                    break;
                }
            }
            if (!achou) {
                posicao++;
            }
        }
        int adj = 0;
        while (adj < grafo.length) {// verifica todas as adjacencias do aeroporto de origem
            if (grafo[posicao][adj] != null && !(visitados.get(grafo[posicao][adj].destino) == true)) {
                visitados.replace(grafo[posicao][adj].destino, true);// muda o aeroporto de destino como ja visitado
                boolean result = CaminhoRecursivo(grafo[posicao][adj].destino, aeroportoDestino, visitados, print);
                if (result == true) {
                    
                    print.add(aeroportoOrigem);
                    return true;
                }
            }

            adj++;
        }

        return false;
    }

    public Map AGM(String Origem, Arquivo aeroporto) {
        List<String> atuais = new ArrayList<String>();
        Map<String, Double> distancia = new HashMap<String, Double>();//armazena o aeroporto e seu menor vertice para chegar nele
        Map<String, String> aresta = new HashMap<String, String>();//armazena pai e filho atuais referentes a distancia.
        Map<String, String> vertices = new HashMap<String, String>();//armazena o vertice filho(key) e seu pai(valor), usada para mostrar de onde ele parte e pra onde ele chega

        atuais.add(Origem);// inicializa a fila dos atuais com o aeroporto inicial
        vertices.put(Origem, " ");// adiciona o aeroporto de origem na lista de vertices
        boolean stop = false;
        while (atuais.size() < aeroporto.listadeAeroportos.size()) {
            distancia.clear();
            aresta.clear();
            int controle = 0;//indice do aeroporto visitado
            while (controle < atuais.size()) {
                int posicao;
                boolean parada = false;
                for (posicao = 0; posicao < grafo.length; posicao++) {
                    if (aeroporto.listadeAeroportos.get(posicao).aeroportos.equals(atuais.get(controle))) {// encontra o seu indice na matriz
                        break;
                    }
                }
                int adj;
                for (adj = 0; adj < grafo.length; adj++) {
                    if (grafo[posicao][adj] != null) {
                        if (!atuais.contains(grafo[posicao][adj].destino)) {
                            if (distancia.get(grafo[posicao][adj].destino) != null) {// se o vertice já estiver na lista de distancia
                                if (distancia.get(grafo[posicao][adj].destino) > grafo[posicao][adj].distancia) {// e se a nova distancia da adjcencia for menor que a atual do vertice 
                                    //se atualiza os valores
                                    distancia.replace(grafo[posicao][adj].destino, grafo[posicao][adj].distancia);
                                    aresta.replace(grafo[posicao][adj].destino, atuais.get(controle));
                                }
                            } else {// caso seja a primeira aeresta encontrada que liga os vertices,apenas insere
                                distancia.put(grafo[posicao][adj].destino, grafo[posicao][adj].distancia);
                                aresta.put(grafo[posicao][adj].destino, atuais.get(controle));
                            }
                        }
                    }
                }
                controle++;
            }
            double min = Double.MAX_VALUE;
            String aeroMin = null;
            if (distancia.size() > 0) {
                Iterator<String> i = distancia.keySet().iterator();
                // procura na adjacencias do vertice trabalhado, o menor caminho
                while (i.hasNext()) {
                    String chave = i.next();
                    if (distancia.get(chave) < min) {
                        min = distancia.get(chave);
                        aeroMin = chave;
                    }

                }
                atuais.add(aeroMin);// coloca o vertice mais proximo na lista de atuais para verificar suas adjacencias
                vertices.put(aeroMin, aresta.get(aeroMin));// adciono o pai(vertice atual) e seu filho(vertice mais proximo)
            } else {
                System.out.println("Esse grafo e disconexo");
                return null;
            }
        }

        return vertices;

    }

    void VerificarConexao(String Origem, Arquivo aeroporto) {
        Map<String, String> result = new HashMap<String, String>();
        result = AGM(Origem, aeroporto);
        if (result != null) {
            for (Entry<String, String> v : result.entrySet()) {// mostro todas as ligaçoes da AGM, mas não sai em ordem pois o Mep não mostra os valores em ordem
                System.out.println("Pai: " + v.getValue() + ", Filho: " + v.getKey());
            }
            System.out.println("E possivel apartir desse aeroporto atingir todos os demais");
            encontrarCritico(aeroporto);
        } else {
            System.out.println("Não e possivel apartir desse aeroporto atingir todos os demais");
        }
    }

    public void encontrarCritico(Arquivo aeroporto) {
        Map<String, Boolean> visitados = new HashMap<String, Boolean>();
        for (int i = 0; i < grafo.length; i++) {
            visitados.put(aeroporto.listadeAeroportos.get(i).aeroportos, false);

        }

        for (int i = 0; i < grafo.length; i++) {
            // busca todos os vetices e busca os que são criticos
            visitados.clear();
            for (int j = 0; j < grafo.length; j++) {
                visitados.put(aeroporto.listadeAeroportos.get(j).aeroportos, false);
            }
            // tentando descobrir se o aeroporto i é funil
            // marca ele como visitado (assim a busca nao percorrerá nele)
            visitados.replace(aeroporto.listadeAeroportos.get(i).aeroportos, true);

            if (i == 0) { // outro aeroporto, que nao seja i, é necessário para iniciar a busca
                visitados.replace(aeroporto.listadeAeroportos.get(1).aeroportos, true);
                VisitarProfundidade(aeroporto.listadeAeroportos.get(1).aeroportos, visitados);
            } else {
                visitados.replace(aeroporto.listadeAeroportos.get(0).aeroportos, true);
                VisitarProfundidade(aeroporto.listadeAeroportos.get(0).aeroportos, visitados);
            }
            for (int j = 0; j < grafo.length; j++) {
                if (!visitados.get(aeroporto.listadeAeroportos.get(j).aeroportos)) {// se o eroporto i for critico o j não será encontrado
                    System.out.println("O aeroporto " + aeroporto.listadeAeroportos.get(i).aeroportos + " critico");
                    break;
                }

            }
        }
    }

    private void VisitarProfundidade(String aeroportoOrigem, Map<String, Boolean> visitados) {
        //busca em profundidade para encontrar o maximo numero de vertices possiveis através da recursão
        //usado no encontra critico
        int posicao = 0;
        boolean achou = false;
        while (posicao < grafo.length) {
            if (achou == true) {
                break;
            }
            for (int j = 0; j < grafo.length; j++) {
                if (grafo[posicao][j] != null && grafo[posicao][j].origem.equals(aeroportoOrigem)) {
                    achou = true;
                    break;
                }
            }
            if (!achou) {
                posicao++;
            }
        }
        int adj = 0;
        while (adj < grafo.length) {
            if (grafo[posicao][adj] != null && !(visitados.get(grafo[posicao][adj].destino) == true)) {
                visitados.replace(grafo[posicao][adj].destino, true);
                VisitarProfundidade(grafo[posicao][adj].destino, visitados);
            }
            adj++;
        }
    }

    void circuito(String Origem, Arquivo aeroporto) {
        Map<String, String> result = new HashMap<String, String>();
        result = AGM(Origem, aeroporto);
        if (result != null) {
            
            for (Entry<String, String> v : result.entrySet()) {// mostro todas as ligaçoes da AGM, mas não sai em ordem pois o Mep não mostra os valores em ordem
                System.out.println("Pai: " + v.getValue() + ", Filho: " + v.getKey());

            }
            List<String> visitados = new ArrayList<String>(); // vertices que já estão percorridos
            List<String> caminho = new ArrayList<String>();// lista que parte da origem e volta caminho maior
            List<String> subcaminho = new ArrayList<String>();// pequeno caminho (pilha)
            visitados.add(Origem);
            for (int i = 0; i < aeroporto.listadeAeroportos.size(); i++) {// verificando se não contem o aeroporto
                if (!visitados.contains(aeroporto.listadeAeroportos.get(i).aeroportos)) {
                    subcaminho.clear();
                    subcaminho.add(aeroporto.listadeAeroportos.get(i).aeroportos);
                    while(!subcaminho.get(subcaminho.size()-1).equals(Origem)){// crio subcaminho ate um nó nao vizitado
                        String anterior = result.get(subcaminho.get(subcaminho.size()-1));
                        subcaminho.add(anterior);
                        visitados.add(anterior);
                    }
                    for (int j = subcaminho.size()-1; j >= 0; j--) {//adciono o subcaminho "indo"
                          caminho.add(subcaminho.get(j));
                         
                    }
                    for (int j = 1; j < subcaminho.size()-1; j++) {//adciono o subcaminho "voltando"
                       caminho.add(subcaminho.get(j)) ;
                    } 
                        
                }

            }
            caminho.add(Origem);
            for (int i = 0; i < caminho.size(); i++) {// printo o todo o caminho percorrido de indo e voltando por todo o grafo
                System.out.print(caminho.get(i)+", ");
            }
            System.out.println("");
            for(int i = 0; i < caminho.size()-1;i++){
                for (int j = 0; j < caminho.size(); j++) {// verifica se é um circuito Hamiltoniano através de vertices que si repetem
                    if(caminho.get(i).equals(caminho.get(j))){
                        System.out.println("Esse rota nao e um circuito Hamiltoniano");
                        return;
                    }
                }
            }
            System.out.println("Esse rota e um circuito Hamiltoniano");
            

        } else {
            System.out.println("Não e possivel apartir desse aeroporto atingir todos os demais");
        }
    }
}
