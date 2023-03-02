package com.mycompany.trabalhoed;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Voos {

    public String origem;
    public String destino;
    public int numVoo;
    public double distancia;
    public int numParadas;
    public int duracao;
    public String hrSaida;
    public String hrChegada;
    public int qtdParadas;
    public List<Voos> ListadeVoos;
    public List<Voos>[] grafo;

    public Voos() {
        ListadeVoos = new ArrayList<>();
        grafo = new ArrayList[23];
    }

    public void grafoVoo(Arquivo aeroporto) {
        for (int i = 0; i < 23; i++) {
            grafo[i] = new ArrayList<Voos>();
        }
        int j = 0;
        for (int i = 0; i < aeroporto.listadeAeroportos.size(); i++) {
            boolean aux = true;
            while (aux == true) {
                if (j != this.ListadeVoos.size()) {//encontro todos os voos que partem da mesma origem e adiciono na lista
                    if (aeroporto.listadeAeroportos.get(i).aeroportos.equals(this.ListadeVoos.get(j).origem)) {
                        grafo[i].add(this.ListadeVoos.get(j));
                    } else {
                        aux = false;
                    }
                } else {
                    return;
                }
                j++;
            }
        }
    }

    public void VoosDiretos(String Origem) {
        int posicao;
        for (posicao = 0; posicao < grafo.length; posicao++) {// encontra a posicao do aeroporto no grafo
            if (grafo[posicao].get(0).origem.equals(Origem)) {
                break;
            }
        }

        for (int adj = 0; adj < grafo[posicao].size(); adj++) {//verifica se nas suas adjacencias tem algum voo direto e mostra pra qual aeroporto esse voo tem como destino
            if (grafo[posicao].get(adj).qtdParadas == 0) {
                System.out.print(grafo[posicao].get(adj).destino + ", ");
            }
        }
        System.out.println();
    }

    public void menorCaminhoDuracao(String Origem, String Destino, Arquivo aeroporto) {
        //essa função utiliza do algortmo de dijkstra para encontrar o menor caminho
        Map<String, Double> distancia = new HashMap<String, Double>();

        for (int i = 0; i < grafo.length; i++) {// inicializa a distancia como infinito
            distancia.put(aeroporto.listadeAeroportos.get(i).aeroportos, Double.MAX_VALUE);
        }
        distancia.replace(Origem, 0.0);// muda a distancia do origem para a menor possivel para que o algortmo comece dele

        Map<String, Boolean> visitados = new HashMap<String, Boolean>();
        for (int i = 0; i < grafo.length; i++) {
            visitados.put(aeroporto.listadeAeroportos.get(i).aeroportos, false);// inicializa todos os aeroportos como não visitados
        }
        visitados.replace(Origem, true);
        List atuais = new ArrayList<String>();// lista que armazena todos os vertices que vamos prabalhar
        atuais.add(Origem);// coloca a origem como o primeiro

        Map<String, String> antecessor = new HashMap<String, String>();// lista que armazena o vertice e o seu pai
        for (int i = 0; i < grafo.length; i++) {
            antecessor.put(aeroporto.listadeAeroportos.get(i).aeroportos, "*");//inicializando os vertices sem um antecessor
        }

        while (atuais.size() > 0) {

            int posicao;

            for (posicao = 0; posicao < grafo.length; posicao++) {
                if (grafo[posicao].get(0).origem.equals(atuais.get(0))) {// encontra a posicao no grafo do vertice que vamos trabalhar
                    break;
                }
            }

            int adj;
            for (adj = 0; adj < grafo[posicao].size(); adj++) {

                double distanciaConhecida = distancia.get(grafo[posicao].get(adj).destino);
                double distanciaCalculada = grafo[posicao].get(adj).duracao + distancia.get(atuais.get(0));

                //verifica se a distancia entre para vertice vizinho e menor que a atual distancia
                if (distanciaCalculada < distanciaConhecida) {
                    distancia.replace(grafo[posicao].get(adj).destino, distanciaCalculada);
                    antecessor.replace(grafo[posicao].get(adj).destino, (String) (atuais.get(0)));
                }
                //verifica se o vertice não foi visitado antes
                if (visitados.get(grafo[posicao].get(adj).destino) == false) {
                    visitados.replace(grafo[posicao].get(adj).destino, true);
                    atuais.add(grafo[posicao].get(adj).destino);
                }

            }
          atuais.remove(0);

        }

        
        ArrayList<String> pilha = new ArrayList<String>();
        pilha.add(Destino);
        while (!pilha.get(pilha.size() - 1).equals(Origem)) {
            pilha.add(antecessor.get(pilha.get(pilha.size() - 1)));
        }
        while(pilha.size()> 0){
        System.out.print(pilha.remove(pilha.size() - 1)+", ");
        }
        
    }
    
    
    public void menorCaminhoDistancia(String Origem, String Destino, Arquivo aeroporto) {
        
        Map<String, Double> distancia = new HashMap<String, Double>();

        for (int i = 0; i < grafo.length; i++) {
            distancia.put(aeroporto.listadeAeroportos.get(i).aeroportos, Double.MAX_VALUE);
        }
        distancia.replace(Origem, 0.0);

        Map<String, Boolean> visitados = new HashMap<String, Boolean>();
        for (int i = 0; i < grafo.length; i++) {
            visitados.put(aeroporto.listadeAeroportos.get(i).aeroportos, false);
        }
        visitados.replace(Origem, true);
        List atuais = new ArrayList<String>();
        atuais.add(Origem);

        Map<String, String> antecessor = new HashMap<String, String>();
        for (int i = 0; i < grafo.length; i++) {
            antecessor.put(aeroporto.listadeAeroportos.get(i).aeroportos, "*");
        }

        while (atuais.size() > 0) {

            int posicao;

            for (posicao = 0; posicao < grafo.length; posicao++) {
                if (grafo[posicao].get(0).origem.equals(atuais.get(0))) {
                    break;
                }
            }

            int adj;
            for (adj = 0; adj < grafo[posicao].size(); adj++) {

                double distanciaConhecida = distancia.get(grafo[posicao].get(adj).destino);
                double distanciaCalculada = grafo[posicao].get(adj).distancia + distancia.get(atuais.get(0));

                //verifica se a distancia entre para vertice vizinho e menor que a atual distancia
                if (distanciaCalculada < distanciaConhecida) {
                    distancia.replace(grafo[posicao].get(adj).destino, distanciaCalculada);
                    antecessor.replace(grafo[posicao].get(adj).destino, (String) (atuais.get(0)));
                }
                //verifica se o vertice não foi visitado antes
                if (visitados.get(grafo[posicao].get(adj).destino) == false) {
                    visitados.replace(grafo[posicao].get(adj).destino, true);
                    atuais.add(grafo[posicao].get(adj).destino);
                }

            }
            atuais.remove(0);

        }
        ArrayList<String> pilha = new ArrayList<String>();
        pilha.add(Destino);
        while (!pilha.get(pilha.size() - 1).equals(Origem)) {
            pilha.add(antecessor.get(pilha.get(pilha.size() - 1)));
        }
        while(pilha.size()> 0){
        System.out.print(pilha.remove(pilha.size() - 1)+", ");
        }
    }
}
