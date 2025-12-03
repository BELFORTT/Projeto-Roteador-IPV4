package br.edu.ifpb.poo.roteador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public class Roteador {
    
    // CORREÇÃO: O Roteador DEVE ter uma instância da TabelaDeRotas
    // para poder usar o algoritmo de Longest Match depois.
    private final TabelaDeRotas tabelaDeRotas;
    
    // Lista de interfaces (Lógica antiga que você pediu)
    private final List<InterfaceFisica> interfaces;
    
    private boolean modoExibicaoCIDR = false; 

    public Roteador() {
        this.tabelaDeRotas = new TabelaDeRotas(); // Inicializa a tabela
        this.interfaces = new ArrayList<>();      // Inicializa a lista
    }

    // UC01 - Cadastrar Interface
    public boolean cadastrarInterface(String nome, String ip) {
        // Verifica duplicidade percorrendo a lista
        for (InterfaceFisica i : this.interfaces) {
            if (i.getNome().equalsIgnoreCase(nome)) {
                return false; 
            }
        }
        
        try {
            InterfaceFisica novaInterface = new InterfaceFisica(nome, ip);
            this.interfaces.add(novaInterface);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar interface: " + e.getMessage());
            return false;
        }
    }
    
    // Método auxiliar de busca
    public InterfaceFisica buscarInterface(String nome) {
        for (InterfaceFisica interfac : this.interfaces) {
            if (interfac.getNome().equalsIgnoreCase(nome)) {
                return interfac;
            }
        }
        return null;
    }

    // Alias
    public InterfaceFisica getInterface(String nome) {
        return buscarInterface(nome);
    }

    // UC02 - Cadastrar Rota
    public boolean cadastrarRota(String destino, String gateway, String mascara, InterfaceFisica interfac) {
        if (interfac == null) return false;
        
        try {
            byte[] destinoByte = IpUtils.stringPraBytes(destino);
            byte[] mascaraByte = IpUtils.stringPraBytes(mascara);

            // Verifica duplicidade olhando dentro da TabelaDeRotas
            for (Rota r : tabelaDeRotas.getRotas()) {
                if (Arrays.equals(r.getEnderecoDestino(), destinoByte) && 
                    Arrays.equals(r.getMascaraDeSubRede(), mascaraByte) && 
                    r.getInterfac().getNome().equalsIgnoreCase(interfac.getNome())) {
                    return false; 
                }
            }
            
            Rota novaRota = new Rota(destino, gateway, mascara, interfac);
            this.tabelaDeRotas.addRota(novaRota); // Adiciona na tabela certa!
            return true;

        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao criar rota: " + e.getMessage());
            return false;
        }
    }

    // Métodos de acesso para o Menu (Pegando da Tabela)
    public List<Rota> getRotas() {
        return tabelaDeRotas.getRotas();
    }

    // UC03 - Visualizar
    public String visualizarTabelaDeRotas() {
        if (tabelaDeRotas.getRotas().isEmpty()) {
            return "Tabela de rotas vazia.";
        }

        StringBuilder tabela = new StringBuilder();
        tabela.append("--- Tabela de Rotas (Modo: ").append(modoExibicaoCIDR ? "CIDR" : "Máscara Decimal").append(") ---\n");

        tabela.append("================================================================================\n");
        tabela.append("| ").append(String.format("%-18s", "Destino")).append(" | ");
        tabela.append(String.format("%-18s", "Máscara")).append(" | ");
        tabela.append(String.format("%-18s", "Gateway")).append(" | ");
        tabela.append(String.format("%-10s", "Interface")).append(" |\n");
        tabela.append("================================================================================\n");

        for (Rota rota : tabelaDeRotas.getRotas()) {

            String destino;

            if (modoExibicaoCIDR) {
                // Se o botão tá ligado, chama o método especial da Rota
                destino = rota.getDestinoCIDR(); 
            } else {
                // Se tá desligado, chama o normal
                destino = IpUtils.bytesParaString(rota.getEnderecoDestino());
            }
            String mascara = IpUtils.bytesParaString(rota.getMascaraDeSubRede());     
            // Tratamento visual do Gateway
            String gwString = IpUtils.bytesParaString(rota.getEnderecoGateway());
            String gateway = gwString.equals("0.0.0.0") ? "-" : gwString;

            String nomeInterface = (rota.getInterfac() != null) ? rota.getInterfac().getNome() : "N/A";

            tabela.append("| ").append(String.format("%-18s", destino)).append(" | ");
            tabela.append(String.format("%-18s", mascara)).append(" | ");
            tabela.append(String.format("%-18s", gateway)).append(" | ");
            tabela.append(String.format("%-10s", nomeInterface)).append(" |\n");
        }

        tabela.append("================================================================================");
        return tabela.toString();
    }

    // UC04 - Alterar Rota
    public Rota alterarRota(int indiceRota, String novoDestino, String novoGateway, String novaMascara, String nomeNovaInterface) {
        List<Rota> lista = tabelaDeRotas.getRotas();
        
        if (indiceRota < 0 || indiceRota >= lista.size()) return null;

        Rota rotaAntiga = lista.get(indiceRota);

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

        try {
            Rota novaRota = new Rota(destinoString, gatewayString, mascaraString, interfaceFinal);
            lista.set(indiceRota, novaRota); // Atualiza na lista oficial
            return novaRota;
        } catch (Exception e) {
            System.out.println("Erro ao alterar: " + e.getMessage());
            return null;
        }
    }

    // UC05 - Excluir Rota
    public void excluirRota(int indice) {
        if (indice >= 0 && indice < tabelaDeRotas.getRotas().size()) {
            tabelaDeRotas.getRotas().remove(indice);
        }
    }

    // UC06 - Configurar Exibição
    public void setModoExibicaoCIDR(boolean isCIDR) {
        this.modoExibicaoCIDR = isCIDR;
    }
    
    // UC07 - Roteamento
    public Rota rotearDatagrama(String ipDestino) {
        // AGORA SIM!!!
        return this.tabelaDeRotas.encontrarMelhorRota(ipDestino);
    }
    
    // UC08 - Resetar
    public void resetarTabela() {
        this.tabelaDeRotas.resetarTabela();
    }
}
