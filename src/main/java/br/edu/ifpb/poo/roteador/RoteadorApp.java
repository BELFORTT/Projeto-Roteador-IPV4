package br.edu.ifpb.poo.roteador;

import java.util.Scanner;
import java.util.Scanner;

public class RoteadorApp {
    public static void main(String[] args) {
        Roteador roteador = new Roteador();
        Scanner in = new Scanner(System.in);

        OUTER:
        while (true) {
            System.out.print("\nMenu\n" +
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
                    "Digite a opção que você deseja: ");
            
            // Tratamento básico para entrada não inteira
            if (!in.hasNextInt()) {
                System.out.println("Por favor, digite um número.");
                in.next(); continue;
            }
            int opc = in.nextInt();
            
            switch (opc) {
                case 1 -> {
                    System.out.print("Digite o nome da interface: ");
                    String nome = in.next();
                    System.out.print("Digite o ip da interface: ");
                    String ip = in.next();
                    if(roteador.cadastrarInterface(nome, ip)) {
                        System.out.println("Interface cadastrada!");
                    } else {
                        System.out.println("Erro: Interface já existe ou IP inválido.");
                    }
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

                    // Usa o alias 'buscarInterface' que criamos no Roteador
                    InterfaceFisica interfac = roteador.buscarInterface(nomeInterface);

                    if (interfac != null) {
                        boolean tudoCerto = roteador.cadastrarRota(destino, gateway, mascara, interfac);
                        if (tudoCerto) {
                            System.out.println("Rota cadastrada com sucesso.");
                        } else {
                            System.out.println("Erro: Rota duplicada ou dados inválidos.");
                        }
                    } else {
                        System.out.println("Interface não encontrada. Cadastre-a primeiro na opção 1.");
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
                        in.nextLine(); // Consumir o enter

                        int indiceArray = indice - 1;

                        if (indiceArray < 0 || indiceArray >= numRotas) {
                            System.out.println("Número inválido");
                            break;
                        }

                        Rota rotaAtual = roteador.getRotas().get(indiceArray);

                        // NÃO usamos mais bytesParaString, pois já é String!
                        System.out.println("Destino atual: " + rotaAtual.getEnderecoDestino());
                        System.out.print("Novo Destino (enter para manter): ");
                        String novoDestino = in.nextLine().trim();

                        System.out.println("Máscara atual: " + rotaAtual.getMascaraDeSubRede());
                        System.out.print("Nova Mascara (enter para manter): ");
                        String novaMascara = in.nextLine().trim();

                        System.out.println("Gateway atual: " + rotaAtual.getEnderecoGateway());
                        System.out.print("Novo Gateway (enter para manter): ");
                        String novoGateway = in.nextLine().trim();

                        System.out.println("Interface atual: " + rotaAtual.getInterfac().getNome());
                        System.out.print("Nova Interface (Enter para manter): ");
                        String nomeNovaInterface = in.nextLine().trim();
                        
                        Rota rotaAlterada = roteador.alterarRota(indiceArray, novoDestino, novoGateway, novaMascara, nomeNovaInterface);

                        if (rotaAlterada != null) {
                            System.out.println("\nRota atualizada com sucesso!");
                        } else {
                            System.out.println("Falha na alteração (dados inválidos).");
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
                            System.out.println("Número inválido");
                            break;
                        }

                        Rota rotaExcluir = roteador.getRotas().get(indiceArray);

                        // Correção: Usando Strings diretas
                        System.out.println("Confirmar a exclusão da Rota: ");
                        System.out.println("Destino: " + rotaExcluir.getEnderecoDestino());
                        System.out.println("Máscara: " + rotaExcluir.getMascaraDeSubRede());
                        System.out.println("Interface: " + rotaExcluir.getInterfac().getNome());

                        System.out.print("Confirma a exclusão? (S/N): ");
                        String confirmacao = in.nextLine().trim().toUpperCase();

                        if (confirmacao.equals("S")) {
                            roteador.excluirRota(indiceArray);
                            System.out.println("Rota excluída com sucesso!");
                        } else {
                            System.out.println("Operação cancelada.");
                        }
                    }
                }
                case 6 ->{
                    System.out.println("--- Configurar Exibição (UC06) ---");
                    System.out.println("Escolha o formato de exibição da tabela:");
                    System.out.println("1 - Máscara de Sub-rede (Ex: 255.255.255.0)");
                    System.out.println("2 - Notação CIDR (Ex: /24)");
                    System.out.print("Opção: ");
                        
                    int modo = in.nextInt();
                        
                    if (modo == 1) {
                        roteador.setModoExibicaoCIDR(false);
                        System.out.println("Modo alterado para: Máscara Decimal.");
                    } else if (modo == 2) {
                        roteador.setModoExibicaoCIDR(true);
                        System.out.println("Modo alterado para: Notação CIDR.");
                    } else {
                        System.out.println("Opção inválida.");
                    }
                }

                case 7 -> {
                     System.out.print("Digite o IP de destino do pacote: ");
                     String ipPacote = in.next();
                     // Chama o método do roteador que usa a tabela de rotas
                     Rota melhorRota = roteador.rotearDatagrama(ipPacote);
                     
                     if (melhorRota != null) {
                         System.out.println("\n--- Roteamento ---");
                         System.out.println("Pacote para " + ipPacote);
                         System.out.println("Enviado via interface: " + melhorRota.getInterfac().getNome());
                         if (melhorRota.getGatewayNumerico() != 0) {
                             System.out.println("Próximo Salto (Gateway): " + melhorRota.getEnderecoGateway());
                         } else {
                             System.out.println("Conexão Direta (On-link)");
                         }
                     } else {
                         System.out.println("❌ Host inalcançável (Nenhuma rota encontrada).");
                     }
                }
                case 8 -> {
                    System.out.println("--- Resetar Tabela (UC08) ---");
                    System.out.println("Tem certeza que deseja apagar TODAS as rotas?");
                    System.out.print("Digite 'S' para confirmar: ");
                    String confirmacao = in.next();
                    
                    if (confirmacao.equalsIgnoreCase("S")) {
                        roteador.resetarTabela(); 
                        System.out.println("Tabela de rotas resetada com sucesso! [cite: 88]");
                    } else {
                        System.out.println("Operação cancelada.");
                    }
                }

                case 0 -> {
                    break OUTER;
                }
                default -> {
                    System.out.println("Opção inválida.");
                }
            }
        }
        in.close();
    }
}