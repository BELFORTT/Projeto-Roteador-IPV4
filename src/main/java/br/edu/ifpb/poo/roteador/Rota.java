package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rota {
    private byte[] enderecoDestino;
    private byte[] enderecoGateway;
    private byte[] mascaraDeSubRede;
    private InterfaceFisica interfac;

    public Rota(String destino, String gateway, String mascara, InterfaceFisica interfac) {

        this.enderecoDestino = IpUtils.stringPraBytes(destino);
        this.mascaraDeSubRede = IpUtils.stringPraBytes(mascara);

        // Lógica do Gateway (0.0.0.0 ou nulo)
        if (gateway != null && !gateway.isEmpty() && !gateway.equals("0.0.0.0") && !gateway.equals("-")) {
            this.enderecoGateway = IpUtils.stringPraBytes(gateway);
        } else {
            // Cria um array de bytes zerado {0, 0, 0, 0}
            this.enderecoGateway = new byte[4]; 
        }

        this.interfac = interfac;
    }

    @Override
    public String toString() {
        // Usamos bytesParaString para exibir bonito
        String gatewayStr = IpUtils.bytesParaString(enderecoGateway);
        if (gatewayStr.equals("0.0.0.0")) gatewayStr = "On-link";

        return "Destino: " + IpUtils.bytesParaString(enderecoDestino) + 
               " | Máscara: " + IpUtils.bytesParaString(mascaraDeSubRede) + 
               " | Gateway: " + gatewayStr + 
               " | Interface: " + (interfac != null ? interfac.getNome() : "N/A");
    }
}