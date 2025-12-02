package br.edu.ifpb.poo.roteador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class Roteador {
    private final TabelaDeRotas tabelaDeRotas;
    private final Map<String, InterfaceFisica> interfaces;
    
    private boolean modoExibicaoCIDR = false; 

    public Roteador() {
        this.tabelaDeRotas = new TabelaDeRotas();
        this.interfaces = new HashMap<>();
    }

    // UC01 - Cadastrar Interface
    public boolean cadastrarInterface(String nome, String ip) {
        if (this.interfaces.containsKey(nome.toLowerCase())) {
            return false;
        } 
        // Valida o IP ao criar o objeto (se for inválido, lança exceção)
        try {
            InterfaceFisica novaInterface = new InterfaceFisica(nome, ip);
            this.interfaces.put(nome.toLowerCase(), novaInterface);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar interface: " + e.getMessage());
            return false;
        }
    }
    
    public InterfaceFisica getInterface(String nome) {
        return this.interfaces.get(nome.toLowerCase());
    }

    // Alias para o RoteadorApp que chama "buscarInterface"
    public InterfaceFisica buscarInterface(String nome) {
        return getInterface(nome);
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

    // UC02 - Cadastrar Rota
    public boolean cadastrarRota(String destino, String gateway, String mascara, InterfaceFisica interfac) {
        if (interfac == null) return false;
        
        try {
            Rota novaRota = new Rota(destino, gateway, mascara, interfac);
            
            // Verifica duplicidade
            for (Rota r : tabelaDeRotas.getRotas()) {
                if (r.getDestinoNumerico() == novaRota.getDestinoNumerico() &&
                    r.getPrefixLength() == novaRota.getPrefixLength() &&
                    r.getInterfac().getNome().equalsIgnoreCase(interfac.getNome())) {
                    return false; 
                }
            }
            
            this.tabelaDeRotas.addRota(novaRota);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao criar rota: " + e.getMessage());
            return false;
        }
    }

    // Métodos de acesso para o Menu
    public List<Rota> getRotas() {
        return tabelaDeRotas.getRotas();
    }

// No arquivo Roteador.java

    public String visualizarTabelaDeRotas() {
        if (tabelaDeRotas.getRotas().isEmpty()) {
            return "Tabela de rotas vazia.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("--- Tabela de Rotas (Modo: ").append(modoExibicaoCIDR ? "CIDR" : "Máscara Decimal").append(") ---\n");
        
        int i = 1;
        for (Rota r : tabelaDeRotas.getRotas()) {
            sb.append(i++).append(". ");
            
            // LÓGICA DO UC06: Decidir como mostrar o destino
            if (modoExibicaoCIDR) {
                // Exibe: 192.168.0.0/24
                sb.append("Destino: ").append(r.getDestinoCIDR());
            } else {
                // Exibe: 192.168.0.0 | Máscara: 255.255.255.0
                sb.append("Destino: ").append(r.getEnderecoDestino())
                .append(" | Máscara: ").append(r.getMascaraDeSubRede());
            }

            sb.append(" | Gateway: ").append(r.getEnderecoGateway() != null ? r.getEnderecoGateway() : "On-link");
            sb.append(" | Interface: ").append(r.getInterfac().getNome()).append("\n");
        }
        return sb.toString();
    }

    // UC04 - Alterar Rota
    public Rota alterarRota(int indice, String novoDestino, String novoGateway, String novaMascara, String nomeNovaInterface) {
        List<Rota> lista = tabelaDeRotas.getRotas();
        if (indice < 0 || indice >= lista.size()) return null;

        Rota rotaAntiga = lista.get(indice);

        // Se o usuário deu Enter (vazio), mantém o antigo
        String destinoFinal = novoDestino.isEmpty() ? rotaAntiga.getEnderecoDestino() : novoDestino;
        String gatewayFinal = novoGateway.isEmpty() ? rotaAntiga.getEnderecoGateway() : novoGateway;
        String mascaraFinal = novaMascara.isEmpty() ? rotaAntiga.getMascaraDeSubRede() : novaMascara;
        
        InterfaceFisica interfaceFinal = rotaAntiga.getInterfac();
        if (!nomeNovaInterface.isEmpty()) {
            InterfaceFisica novaIf = getInterface(nomeNovaInterface);
            if (novaIf != null) {
                interfaceFinal = novaIf;
            } else {
                System.out.println("Nova interface não encontrada. Mantendo a anterior.");
            }
        }

        try {
            Rota rotaAtualizada = new Rota(destinoFinal, gatewayFinal, mascaraFinal, interfaceFinal);
            lista.set(indice, rotaAtualizada); // Substitui na lista
            return rotaAtualizada;
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao alterar rota: " + e.getMessage());
            return null;
        }
    }

    // UC05 - Excluir Rota
    public void excluirRota(int indice) {
        if (indice >= 0 && indice < tabelaDeRotas.getRotas().size()) {
            tabelaDeRotas.getRotas().remove(indice);
        }
    }

    // UC07 - Roteamento
    public Rota rotearDatagrama(String ipDestino) {
        return this.tabelaDeRotas.encontrarMelhorRota(ipDestino);
    }
    
    public void resetarTabela() {
        this.tabelaDeRotas.resetarTabela();
    }
}
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
