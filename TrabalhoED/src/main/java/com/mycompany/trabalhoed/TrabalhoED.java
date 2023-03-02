/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.trabalhoed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author costa
 */
public class TrabalhoED {

    private static boolean validaAeroportos(String origem, String destino, Arquivo aeroporto) {
        //função responsavel por validar se o aeroporto informado está presente na lista de aeroportos
        //E usada no main e retorna verdadeiro ou falso
        for (int i = 0; i < aeroporto.listadeAeroportos.size(); i++) {

            if (aeroporto.listadeAeroportos.get(i).aeroportos.equals(origem)) {

                for (int j = 0; j < aeroporto.listadeAeroportos.size(); j++) {
                    if (aeroporto.listadeAeroportos.get(j).aeroportos.equals(destino)) {
                        System.out.println("Dados validos");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws NumberFormatException, IOException {
        System.out.println("=========================================================================================");
        System.out.println("Grafo de Rotas:");
        System.out.println("=========================================================================================");
        Rotas rota = new Rotas();
        Arquivo ler = new Arquivo();
        Voos voo = new Voos();
        ler.lerArquivo(rota, voo);
        for (int i = 0; i < rota.ListadeRotas.size(); i++) {
            //System.out.print(rota.ListadeRotas.get(i).qtdVoo+" ");
        }
        rota.grafoRota(ler);
        for (int i = 0; i < rota.grafo.length; i++) {
            System.out.println();
            System.out.print(ler.listadeAeroportos.get(i).aeroportos + "-> ");
            for (int j = 0; j < rota.grafo.length; j++) {
                if (rota.grafo[i][j] == null) {
                } else {
                    System.out.print(rota.grafo[i][j].destino + " ");
                }
            }
        }
        System.out.println("");
        System.out.println("=========================================================================================");
        System.out.println("Grafo de Voos:");
        System.out.println("=========================================================================================");

        voo.grafoVoo(ler);
        for (int i = 0; i < voo.grafo.length; i++) {
            System.out.println();
            System.out.print(ler.listadeAeroportos.get(i).aeroportos + "->");
            for (int j = 0; j < voo.grafo[i].size(); j++) {
                System.out.print(voo.grafo[i].get(j).destino + ",  ");
            }
        }
        boolean aux = true;
        Scanner scanner = new Scanner(System.in);
        while (aux) {
            /*System.out.println("===============Aeroportos===============");
		for(int i= 0; i<ler.listadeAeroportos.size();i++) {
			System.out.println(ler.listadeAeroportos.get(i).aeroportos);
		}*/
            System.out.println("========================================");
            System.out.println("===============MENU===============");
            System.out.println("1-Buscar caminho\n2-Voos diretos\n3-Viagem com menor custo\n4-Verificar aeroporto critico e se atinge todos os outros aeroportos\n5-Circuito\n0-EXIT");
            System.out.print("Digite a opcao desejada: ");
            int opcao = scanner.nextInt();
            switch (opcao) {
                case 1:
                    String aeroportoOrigem,
                     aeroportoDestino;
                    System.out.println("Digite o aeroporto de origem: ");
                    aeroportoOrigem = scanner.next();
                    aeroportoOrigem = aeroportoOrigem.toUpperCase();
                    System.out.println("Digite o aeroporto de destino: ");
                    aeroportoDestino = scanner.next();
                    System.out.println("===========================================");
                    aeroportoDestino = aeroportoDestino.toUpperCase();

                    if (validaAeroportos(aeroportoOrigem, aeroportoDestino, ler)==true) {
                        rota.Caminho(aeroportoOrigem, aeroportoDestino, ler);
                    } else System.out.println("Aeroporto nao existe!");
                        break;
                    
                case 2:
                    System.out.print("Digite o aeroporto que deseja verificar: ");
                    aeroportoOrigem = scanner.next();
                    aeroportoOrigem = aeroportoOrigem.toUpperCase();
                    if (validaAeroportos(aeroportoOrigem, aeroportoOrigem, ler)==true) {
                        voo.VoosDiretos(aeroportoOrigem);
                    } else System.out.println("Aeroporto nao existe!");
                    
                    break;
                case 3:
                    System.out.println("Digite o aeroporto de origem: ");
                    aeroportoOrigem = scanner.next();
                    aeroportoOrigem = aeroportoOrigem.toUpperCase();
                    System.out.println("Digite o aeroporto de destino: ");
                    aeroportoDestino = scanner.next();
                    System.out.println("===========================================");
                    aeroportoDestino = aeroportoDestino.toUpperCase();
                    
                    if (validaAeroportos(aeroportoOrigem, aeroportoDestino, ler)==true) {
                        System.out.println("Menor duracao:");
                    voo.menorCaminhoDuracao(aeroportoOrigem, aeroportoDestino, ler);
                    System.out.println("");
                    System.out.println("Menor Distancia:");
                    voo.menorCaminhoDistancia(aeroportoOrigem, aeroportoDestino, ler);
                    System.out.println("");
                    } else System.out.println("Aeroporto nao existe!");
                    
                    break;
                case 4:
                    System.out.print("Digite o aeroporto de origem: ");
                    aeroportoOrigem = scanner.next();
                    aeroportoOrigem = aeroportoOrigem.toUpperCase();
                     if (validaAeroportos(aeroportoOrigem, aeroportoOrigem, ler)==true) {
                         rota.VerificarConexao(aeroportoOrigem, ler);
                    
                    } else System.out.println("Aeroporto nao existe!");
                    break;
                case 5:
                    System.out.print("Digite o aeroporto de origem: ");
                    aeroportoOrigem = scanner.next();
                    aeroportoOrigem = aeroportoOrigem.toUpperCase();
                     if (validaAeroportos(aeroportoOrigem, aeroportoOrigem, ler)==true) {
                         rota.circuito( aeroportoOrigem, ler) ;
                    } else System.out.println("Aeroporto nao existe!");
                    break;
                case 0:
                    System.exit(0);
                    break;
            }
        }
    }
}
