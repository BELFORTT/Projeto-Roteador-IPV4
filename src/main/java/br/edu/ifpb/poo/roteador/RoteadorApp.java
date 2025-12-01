package br.edu.ifpb.poo.roteador;

import java.util.Scanner;

public class RoteadorApp {
    public static void main(String[] args) {
        Roteador roteador = new Roteador();
        Scanner in = new Scanner(System.in);

        OUTER:
        while (true) {
            System.out.print("Menu\n" +
                    "(1) - Cadastrar Interface\n" +
                    "(2) - Cadastrar Rota\n" +
                    "(3) - Visualizar tabela de rotas\n" +
                    "(4) - Alterar rota\n" +
                    "(5) - Excluir rota\n" +
                    "(6) - Configura exibição das rotas\n" +
                    "(7) - Roteamento para IP\n" +
                    "(8) - Resetar tabela de rotas\n" +
                    "(0) - Sair\n" +
                    "-------------------------------------\n" +
                    "Qual opção você deseja: ");
            int opc = in.nextInt();
            switch (opc) {
                case 1 -> {
                    System.out.print("Digite o nome da interface: ");
                    String nome = in.next();
                    System.out.print("Digite o ip da interface: ");
                    String ip = in.next();
                    System.out.println(roteador.cadastrarInterface(nome, ip));
                    
                }
                case 2 -> {
                    System.out.print("Destino: ");
                    String destino = in.next();
                    System.out.print("Gateway: ");
                    String gateway = in.next();
                    System.out.print("Mascara: ");
                    String mascara = in.next();
                    System.out.print("Nome da interface: ");
                    String nomeInterface = in.next();

                    InterfaceFisica interfac = roteador.buscarInterface(nomeInterface);

                    if (interfac != null) {
                        boolean tudoCerto =  roteador.cadastrarRota(destino, gateway, mascara, interfac);

                        if (tudoCerto) {
                            System.out.println("Rota cadastrada com sucesso.");
                        } else {
                            System.out.println("Essa rota já existe.");
                        }
                    } else {
                        System.out.println("Interface não encontrada. Rota não foi cadastrada.");
                    }
                }
                case 3 -> System.out.println(roteador.visualizaTabelaDeRotas());
                
                case 4 -> System.out.print("Teste");
                case 5 -> System.out.print("Teste");
                case 6 -> System.out.print("Teste");
                case 7 -> System.out.print("Teste");
                case 8 -> System.out.print("Teste");
                case 0 -> {
                    break OUTER;
                }
                default -> {
                    System.out.println("Valor inválido. Tente novamente.");
                }
            }
        }

        in.close();
    }

}
