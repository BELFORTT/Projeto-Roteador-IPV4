package br.edu.ifpb.poo.roteador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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