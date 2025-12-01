package br.edu.ifpb.poo.roteador;

public class IpUtils{
    public static byte[] stringPraBytes(String ip){
        String[] partes = ip.split("\\.");
        byte[] bytes = new byte[4];
        
        for (int i = 0; i<4; i++){
            int valorInt = Integer.parseInt(partes[i]);
            //esse parseInt converte o valor para um numero inteiro!!
            bytes[i] = (byte) valorInt;
        }
        return bytes;
    }
    public static String bytesparaString(byte[] ipBytes){
        String ipFormatado = "";

        for (int i = 0; i<4;i++){
        int valorPositivo = ipBytes[i] & 0xFF;
        ipFormatado += valorPositivo;
        
        if (i>3){
            ipFormatado+= ".";
        }
    }
    return ipFormatado;
    }
}