package br.edu.ifpb.poo.roteador;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeRotas {

    private final List<Rota> rotas = new ArrayList<>();

    public void addRota(Rota novaRota) {
        this.rotas.add(novaRota);
    }

    public List<Rota> getRotas() {
        return rotas;
    }

    public void resetarTabela() {
        this.rotas.clear();
    }

    /**
     * Algoritmo Longest Match ADAPTADO para lógica de Bytes.
     */
    public Rota encontrarMelhorRota(String ipDestino) {
        if (rotas.isEmpty()) {
            return null;
        }

        // 1. Converte o IP de destino para bytes (usando seu IpUtils antigo)
        byte[] ipDestinoBytes = IpUtils.stringPraBytes(ipDestino);

        Rota melhorRota = null;
        int maiorPrefixo = -1; // Guarda o tamanho da máscara vencedora (ex: 24 bits)

        for (Rota rota : rotas) {
            byte[] mascara = rota.getMascaraDeSubRede();
            byte[] destinoRota = rota.getEnderecoDestino();
            
            boolean combina = true;

            // 2. Verifica se o IP combina com a Rota (fazendo o AND byte a byte)
            for (int i = 0; i < 4; i++) {
                // A lógica de rede é: (IP_Destino & Máscara) == Endereço_Rede_Rota
                if ((ipDestinoBytes[i] & mascara[i]) != (destinoRota[i] & mascara[i])) {
                    combina = false;
                    break;
                }
            }

            // 3. Se combinou, verifica se é a "Melhor Rota" (Prefixo mais longo)
            if (combina) {
                int prefixoAtual = contarBitsUm(mascara);
                
                // Se essa máscara for maior que a anterior, ela ganha
                if (prefixoAtual > maiorPrefixo) {
                    maiorPrefixo = prefixoAtual;
                    melhorRota = rota;
                }
            }
        }
        
        return melhorRota;
    }

    // Método auxiliar para contar quantos bits '1' tem na máscara (ex: 255.255.255.0 = 24 bits)
    private int contarBitsUm(byte[] mascara) {
        int bits = 0;
        for (byte b : mascara) {
            // Truque do Java para contar bits em um byte
            bits += Integer.bitCount(b & 0xFF);
        }
        return bits;
    }
}