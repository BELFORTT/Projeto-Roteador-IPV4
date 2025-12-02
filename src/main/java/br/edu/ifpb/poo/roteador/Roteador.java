package br.edu.ifpb.poo.roteador;

import java.util.ArrayList;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class Roteador {
    private final ArrayList<Rota> rotas;
    private final ArrayList<InterfaceFisica> interfaces;

    public Roteador() {
        this.rotas = new ArrayList<>();
        this.interfaces = new ArrayList<>();
    }

    //Método UC01
    public boolean cadastrarInterface(String nome, String ip) {
        for (InterfaceFisica interfaceF : this.interfaces) {
            if (interfaceF.getNome().equalsIgnoreCase(nome)) {
                return false;
            } 
        }
        InterfaceFisica novaInterface = new InterfaceFisica(nome, ip);
        this.interfaces.add(novaInterface);
        return true;
    }

    public InterfaceFisica buscarInterface(String nome) {
        for (InterfaceFisica interfac : this.interfaces) {
            if (interfac.getNome().equalsIgnoreCase(nome)) {
                return interfac;
            }
        }
        return null;
    }

    // Método UC02
    public boolean cadastrarRota(String destino, String gateway, String mascara, InterfaceFisica interfac) {
        byte[] destinoByte = IpUtils.stringPraBytes(destino);
        byte[] mascaraByte = IpUtils.stringPraBytes(mascara);

        for (Rota rota : this.rotas) {
            if (Arrays.equals(rota.getEnderecoDestino(), destinoByte) && Arrays.equals(rota.getMascaraDeSubRede(), mascaraByte) && rota.getInterfac().equals(interfac)) {
                return false;
            }
        }

        Rota novaRota = new Rota(destino, gateway, mascara, interfac);
        this.rotas.add(novaRota);
        return true;
    }

    // Método UC03
    public String visualizarTabelaDeRotas() {
        if (this.rotas.isEmpty()) {
            return "Tabela de rotas vazia";
        }

        String tabela = "";

        tabela += "================================================================================\n";
        tabela += "| " + String.format("%-18s", "Destino") + " | ";
        tabela += String.format("%-18s", "Máscara") + " | ";
        tabela += String.format("%-18s", "Gateway") + " | ";
        tabela += String.format("%-10s", "Interface") + " |\n";
        tabela += "================================================================================\n";

        for (Rota rota : this.rotas) {
            String destino = IpUtils.bytesParaString(rota.getEnderecoDestino());
            String mascara = IpUtils.bytesParaString(rota.getMascaraDeSubRede());
            String gateway;

            if (IpUtils.bytesParaString(rota.getEnderecoGateway()).equals("0.0.0.0")) {
                gateway = "-";
            } else {
                gateway = IpUtils.bytesParaString(rota.getEnderecoGateway());
            }

            String nomeInterface = rota.getInterfac().getNome();

            tabela += "| " + String.format("%-18s", destino) + " | ";
            tabela += String.format("%-18s", mascara) + " | ";
            tabela += String.format("%-18s", gateway) + " | ";
            tabela += String.format("%-10s", nomeInterface) + " |\n";
        }

        tabela += "================================================================================";

        return tabela;

    }
    // Método UC04
    public Rota alterarRota(int indiceRota, String novoDestino, String novoGateway, String novaMascara, String nomeNovaInterface) {
        Rota rotaAntiga = this.rotas.get(indiceRota);

        String destinoString = novoDestino.isEmpty() ? IpUtils.bytesParaString(rotaAntiga.getEnderecoDestino()) : novoDestino;

        String mascaraString = novaMascara.isEmpty() ? IpUtils.bytesParaString(rotaAntiga.getMascaraDeSubRede()) : novaMascara;

        String gatewayString = novoGateway.isEmpty() ? IpUtils.bytesParaString(rotaAntiga.getEnderecoGateway()) : novoGateway;

        InterfaceFisica interfaceAntiga = rotaAntiga.getInterfac();
        InterfaceFisica interfaceFinal;

        if (nomeNovaInterface.isEmpty()) {
            interfaceFinal = interfaceAntiga;
        } else {
            InterfaceFisica interfaceBuscada = buscarInterface(nomeNovaInterface);

            if (interfaceBuscada == null) {
                System.out.println("Não foi encontrada a interface. Deixaremos a antiga!");
                interfaceFinal = interfaceAntiga;
            } else {
                interfaceFinal = interfaceBuscada;
            }
        }

        Rota novaRota = new Rota(destinoString, gatewayString, mascaraString, interfaceFinal);

        this.rotas.set(indiceRota, novaRota);

        return novaRota;
    }
    // Método UC05
    public Rota excluirRota(int indice) {
        return this.rotas.remove(indice);
    }

    // Método UC06
    
    // Método UC07

}
