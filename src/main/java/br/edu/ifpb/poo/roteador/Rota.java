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
            
        // Gateway pode ser vazio em alguns casos, tratamos isso
        if (gateway != null && !gateway.isEmpty()) {
                this.enderecoGateway = IpUtils.stringPraBytes(gateway);
        } else {
            this.enderecoGateway = new byte[4]; // 0.0.0.0
        }
            
        this.mascaraDeSubRede = IpUtils.stringPraBytes(mascara);
        this.interfac = interfac;
        }
}
