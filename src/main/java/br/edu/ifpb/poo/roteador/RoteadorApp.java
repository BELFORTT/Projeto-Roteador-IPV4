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
                case 3 -> {
                    System.out.println(roteador.visualizarTabelaDeRotas());
                }

                case 4 -> {
                    if (roteador.getRotas().isEmpty()) {
                        System.out.println("Tabela de Rotas vazia");
                    } else {
                        System.out.println(roteador.visualizarTabelaDeRotas());

                        int numRotas = roteador.getRotas().size();

                        System.out.print("Digite o número da rota que deseja alterar (1 a " + numRotas + "): ");

                        int indice = in.nextInt();
                        in.nextLine();

                        int indiceArray = indice - 1;

                        if (indiceArray < 0 || indiceArray >= numRotas) {
                            System.out.println("Número de rota inválido");
                            break;
                        }

                        Rota rotaAtual = roteador.getRotas().get(indiceArray);

                        System.out.println("Destino atual: " + IpUtils.bytesParaString(rotaAtual.getEnderecoDestino()));
                        System.out.print("Novo Destino (enter para manter): \n");
                        String novoDestino = in.nextLine().trim();

                        System.out.println("Máscara atual: " + IpUtils.bytesParaString(rotaAtual.getMascaraDeSubRede()));
                        System.out.print("Nova Mascara (enter para manter): \n");
                        String novaMascara= in.nextLine().trim();

                        System.out.println("Gateway atual: " + IpUtils.bytesParaString(rotaAtual.getEnderecoGateway()));
                        System.out.print("Novo Gateway (enter para manter): \n");
                        String novoGateway = in.nextLine().trim();

                        System.out.println("Interface atual: " + rotaAtual.getInterfac().getNome());
                        System.out.print("Nova Interface (Enter para manter): \n");
                        String nomeNovaInterface = in.nextLine().trim();
                        
                        Rota rotaAlterada = roteador.alterarRota(indiceArray, novoDestino, novoGateway, novaMascara, nomeNovaInterface);

                        if (rotaAlterada != null) {
                            System.out.println("\nRota atualizada com sucesso!");
                        } else {
                            System.out.println("Falha na alteração");
                        }
                    }
                }


                case 5 -> {
                    if (roteador.getRotas().isEmpty()) {
                        System.out.println("Tabela de Rotas vazia");
                    } else {
                        System.out.println(roteador.visualizarTabelaDeRotas());

                        int numRotas = roteador.getRotas().size();

                        System.out.print("Digite o número da rota que deseja excluir (1 a " + numRotas + "): ");

                        int indice = in.nextInt();
                        in.nextLine();

                        int indiceArray = indice - 1;

                        if (indiceArray < 0 || indiceArray >= numRotas) {
                            System.out.println("Número de rota inválido");
                            break;
                        }

                        Rota rotaExcluir = roteador.getRotas().get(indiceArray);

                        System.out.println("Confirmar a exclusão da Rota: ");
                        System.out.println("Destino: " + IpUtils.bytesParaString(rotaExcluir.getEnderecoDestino()));
                        System.out.println("Máscara: " + IpUtils.bytesParaString(rotaExcluir.getMascaraDeSubRede()));
                        System.out.println("Interface: " + rotaExcluir.getInterfac().getNome());

                        System.out.print("Confirma a exclusão? (S/N): ");
                        
                        String confirmacao = in.nextLine().trim().toUpperCase();

                        if (confirmacao.equals("S")) {
                            roteador.excluirRota(indiceArray);
                            System.out.println("Rota excluída com sucesso!");
                        } else {
                            System.out.println("Exclusão cancelada pelo usuário.");
                        }
                    }

                    
                }

                case 6 -> System.out.println("Teste");
                case 7 -> System.out.println("Teste");
                case 8 -> System.out.println("Teste");
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
