package br.edu.ifpb.poo.roteador;

import java.util.Scanner;

public class RoteadorApp {
    public static void main(String[] args) {
        Roteador roteador = new Roteador();
        Scanner in = new Scanner(System.in);

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

            if (opc == 1) {
                System.out.print("Digite o nome da interface: ");
                    String nome = in.next();
                    System.out.print("Digite o ip da interface: ");
                    Byte ip = in.nextByte();
                    roteador.cadastrarInterface(nome, ip);
            }

            if (opc == 2) {
                System.out.println("Digite o destino: ");
                    System.out.println("Digite o gateway: ");
                    System.out.println("Digite a mascara");

                    // Como vou pegar a interface correta?

                    roteador.cadastrarRota(null, null, null, null);
            }

            else if (opc == 3) {
                System.out.println("Teste");
            }

            else if (opc == 4) {
                System.out.println("Teste");
            }

            else if (opc == 5) {
                System.out.println("Teste");
            }

            else if (opc == 6) {
                System.out.println("Teste");
            }

            else if (opc == 7) {
                System.out.println("Teste");
            }

            else if (opc == 8) {
                System.out.println("Teste");
            }

            else if (opc == 0) {
                break;
            }
        }

        in.close();
    }

    // UC06

    // UC08
}
