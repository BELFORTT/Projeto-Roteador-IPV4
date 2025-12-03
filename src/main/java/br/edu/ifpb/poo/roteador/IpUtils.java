package br.edu.ifpb.poo.roteador;

public class IpUtils {
    
    // Converte String pra array de bytes
    public static byte[] stringPraBytes(String ip) {
        String[] partes = ip.split("\\.");
        byte[] bytes = new byte[4];
        
        if (partes.length != 4) {
        throw new IllegalArgumentException("Formato inválido (precisa ser A.B.C.D): " + ip);
        }

        for (int i = 0; i < 4; i++) {
            try {
                int valorInt = Integer.parseInt(partes[i]);
                if (valorInt < 0 || valorInt > 255) {
                throw new IllegalArgumentException("Valor fora do limite (0-255): " + valorInt);
                }
                // Isso força o número a caber no byte (mesmo que fique negativo)
                bytes[i] = (byte) valorInt;
            } catch (NumberFormatException e) {
                // Caso digitem algo que não é número
                throw new IllegalArgumentException("IP Inválido: " + ip);
            }
        }
        return bytes;
    }

    // Converte array de bytes de volta para String
    public static String bytesParaString(byte[] ipBytes) {
        String ipFormatado = "";

        for (int i = 0; i < 4; i++) {
            // & 0xFF transforma o byte negativo de volta para positivo (0-255)
            int valorPositivo = ipBytes[i] & 0xFF;
            ipFormatado += valorPositivo;
            
            // Adiciona o ponto apenas entre os números (não no final)
            if (i < 3) { 
                ipFormatado += ".";
            }
        }
        return ipFormatado;
    }
}