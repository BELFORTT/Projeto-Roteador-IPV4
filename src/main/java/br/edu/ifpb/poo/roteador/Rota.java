package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rota {
    // Atributos básicos (String para exibir na tela bonitinho)
    private String enderecoDestino;
    private String enderecoGateway;
    private String mascaraDeSubRede;
    private InterfaceFisica interfac;

    // Atributos essenciais para o Longest Match (Lógica Matemática)
    private final int prefixLength;
    private final long destinoNumerico;
    private final long gatewayNumerico; // <--- ADICIONEI ISSO (Importante para roteamento)

    public Rota(String destino, String gateway, String mascara, InterfaceFisica interfac) {
        
        // 1. Validar e Calcular os valores numéricos
        try {
            // Converte Destino e Máscara
            this.prefixLength = IPUtils.mascaraParaComprimento(mascara);
            this.destinoNumerico = IPUtils.ipParaNumerico(destino);

            // <--- ADICIONEI ESSA LÓGICA DO GATEWAY --->
            // O gateway pode ser nulo ou vazio (rota direta), então tratamos isso:
            if (gateway != null && !gateway.isEmpty() && !gateway.equals("0.0.0.0")) {
                this.gatewayNumerico = IPUtils.ipParaNumerico(gateway);
            } else {
                this.gatewayNumerico = 0; // 0 significa que não tem gateway (é on-link)
            }

        } catch (IllegalArgumentException e) {
            // Se o IP ou a máscara forem inválidos, lança erro para o menu pegar
            throw new IllegalArgumentException("Erro na Rota: " + e.getMessage(), e);
        }

        // 2. Atribuir os atributos de texto (para o Menu/ToString)
        this.enderecoDestino = destino;
        this.enderecoGateway = gateway;
        this.mascaraDeSubRede = mascara;
        this.interfac = interfac;
    }

    // --- Métodos Manuais (Segurança contra falha do Lombok/VS Code) ---

    public int getPrefixLength() {
        return prefixLength;
    }
    
    public long getDestinoNumerico() {
        return destinoNumerico;
    }

    public long getGatewayNumerico() { // <--- Getter do novo campo
        return gatewayNumerico;
    }

    // Método auxiliar para exibição (Ex: 192.168.0.0/24)
    public String getDestinoCIDR() {
        return this.enderecoDestino + "/" + this.prefixLength;
    }

    @Override
    public String toString() {
        return "Destino: " + getDestinoCIDR() + 
               " | Gateway: " + (enderecoGateway != null && !enderecoGateway.isEmpty() ? enderecoGateway : "On-link") + 
               " | Interface: " + (interfac != null ? interfac.getNome() : "N/A");
    }
}