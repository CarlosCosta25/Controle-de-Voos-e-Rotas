package com.mycompany.trabalhoed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Arquivo {

    public String aeroportos;
    public int cordX;
    public int cordY;
    public String fuso;
    public List<Arquivo> listadeAeroportos;

    public Arquivo() {
        listadeAeroportos = new ArrayList<Arquivo>();
    }

    private static double calculaDistancia(int origemX, int origemY, int destinoX, int destinoY) {
        //função responsavel por calcular a distancia entre os aeroportos 
        int cordX = (destinoX - origemX);
        int cordY = (destinoY - origemY);
        double distancia = Math.sqrt((cordX * cordX) + (cordY * cordY));
        return distancia*10;// retorna a distancia em dezenas de Km
    }

    private static int calculaDuracao(String hSaida, String hChegada,String fusoS, String fusoC) {
        //calcula a a duracao do vou do momento da saida do aviao ate o momento de chegada no aeroporto de destino
       DateTimeFormatter df = DateTimeFormatter.ofPattern("hmma");//define o formado da hora que será recebida
        
       ZoneOffset fuso1 = ZoneOffset.of(fusoS);
        ZoneOffset fuso2 = ZoneOffset.of(fusoC);
        
        OffsetTime hora1 = OffsetTime.of(LocalTime.parse(hSaida+"M",df), fuso1);//converte a hora se acaso ela for dps do meio dia
        OffsetTime hora2 = OffsetTime.of(LocalTime.parse(hChegada+"M",df), fuso2);
        
        OffsetTime utc1 = hora1.withOffsetSameInstant(ZoneOffset.UTC);// converte para o fuso do local
        OffsetTime utc2 = hora2.withOffsetSameInstant(ZoneOffset.UTC);
        
        Duration duracao = Duration.between(utc1, utc2);// calcula a duracao do voo
        if(duracao.isNegative()){
            duracao = duracao.plusDays(1);// se o resultado da duracao for negativa, para inverte-la soma-se mais um dia
        }
        
        
        //System.out.println(duracao);
        return (int)duracao.toMinutes();// trasformo a duração em min para trabalhar com double
    }

    public void lerArquivo(Rotas Rotas, Voos Voo) throws NumberFormatException, IOException {
        //essa função e responsavel por ler todo o txt, inserir em uma respectiva lista(aeroporto, rotas,voos) e dar todo o tipo de tratamento necessario aos atributos contidos na lista

        File file = new File("src\\Voos_Original.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));// o arquivo será lido linha por linha
        String ler;// string que vai armazenar todas as linhas do arquivo

        /*
		 * fazendo a leitura e inserindo dados dos aroportos como: as cordenas e sua
		 * sigla"
         */
        int cont = 0;
        while ((ler = br.readLine()).equals("!") != true) {// adciona todas as informações relacionadas ao aeroporto
            Arquivo lerArq = new Arquivo();
            String[] linhaSeparda = ler.split(",");
            lerArq.aeroportos = linhaSeparda[0];
            lerArq.fuso = linhaSeparda[1];
            lerArq.cordX = Integer.valueOf(linhaSeparda[2]);
            lerArq.cordY = Integer.valueOf(linhaSeparda[3]);
            listadeAeroportos.add(lerArq);
        }

        // fazendo a leitura e inserindo dados das rotas como: a origem e o destino
        while ((ler = br.readLine()).equals("#") != true) {
            Rotas rotas = new Rotas();
            String[] linhaSeparda = ler.split(",");
            rotas.origem = linhaSeparda[0];
            rotas.destino = linhaSeparda[1];
            int i, j;
            for (i = 0; i < listadeAeroportos.size(); i++) {// os dois for são usados para encontrar o indice do aeroporto de origem e destino
                if (listadeAeroportos.get(i).aeroportos.equals(rotas.origem)) {
                    break;
                }
            }
            for (j = 0; j < listadeAeroportos.size(); j++) {
                if (listadeAeroportos.get(j).aeroportos.equals(rotas.destino)) {
                    break;
                }

            }
            rotas.distancia = calculaDistancia(listadeAeroportos.get(i).cordX, listadeAeroportos.get(i).cordY, listadeAeroportos.get(j).cordX, listadeAeroportos.get(j).cordY);
            Rotas.ListadeRotas.add(rotas);
        }

        // fazendo a leitura e inserindo dados dos voos em uma lista
        while ((ler = br.readLine()) != null) {
            Voos voos = new Voos();
            String[] linhaSeparda = ler.split(",");
            voos.numVoo = Integer.valueOf(linhaSeparda[0]);
            voos.origem = linhaSeparda[1];
            voos.hrSaida = linhaSeparda[2];
            voos.destino = linhaSeparda[3];
            voos.hrChegada = linhaSeparda[4];
            
            voos.qtdParadas = Integer.valueOf(linhaSeparda[5]);
            int i,j;
            for (i = 0; i < listadeAeroportos.size(); i++) {
                if (listadeAeroportos.get(i).aeroportos.equals(voos.origem)) {// os dois for são usados para encontrar o indice do aeroporto de origem e destino
                    break;
                }
            }
            for (j = 0; j < listadeAeroportos.size(); j++) {
                if (listadeAeroportos.get(j).aeroportos.equals(voos.destino)) {
                    break;
                }

            }
            voos.duracao = calculaDuracao(voos.hrSaida, voos.hrChegada,listadeAeroportos.get(i).fuso, listadeAeroportos.get(j).fuso);
            voos.distancia = calculaDistancia(listadeAeroportos.get(i).cordX, listadeAeroportos.get(i).cordY, listadeAeroportos.get(j).cordX, listadeAeroportos.get(j).cordY);
            Voo.ListadeVoos.add(voos);
        }
    }
}
