package br.edu.ifpb.poo.roteador;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeRotas {

    // A lista de rotas deve ser populada pelos métodos de cadastro
    private final List<Rota> rotas = new ArrayList<>();

    // --- Métodos de Gerenciamento da Tabela ---
    
    public void addRota(Rota novaRota) {
        this.rotas.add(novaRota);
    }
    
    // Método UC03
    public List<Rota> getRotas() {
        return rotas;
    }
    
    // Método UC08
    public void resetarTabela() {
        this.rotas.clear();
    }
    
    // --- Algoritmo Longest Match (UC07) ---

    /**
     * Implementa o algoritmo de Longest Match para encontrar a melhor rota.
     * @param ipDestino O endereço IP de destino do datagrama (ex: "200.129.68.2").
     * @return A rota mais específica encontrada (o Longest Match), ou null se não houver rotas.
     */
    public Rota encontrarMelhorRota(String ipDestino) {
        if (rotas.isEmpty()) {
            return null;
        }
        
        long ipDestinoNumerico = IPUtils.ipParaNumerico(ipDestino);

        Rota melhorRota = null;
        int maxPrefixLength = -1; 

        for (Rota rotaCandidata : rotas) {
            
            int prefixLength = rotaCandidata.getPrefixLength(); 
            long destinoRotaNumerico = rotaCandidata.getDestinoNumerico(); // USANDO O VALOR PRÉ-CALCULADO!
            
            // 1. Criar a máscara binária correspondente ao prefixo.
            long mascaraBinaria = IPUtils.criarMascaraBinaria(prefixLength);

            // 2. Aplica a máscara ao IP de destino.
            long ipDestinoMasked = ipDestinoNumerico & mascaraBinaria;

            // 3. Verifica se há uma correspondência (Match)
            if (ipDestinoMasked == destinoRotaNumerico) {
                
                // 4. Aplica a regra do Longest Match.
                if (prefixLength > maxPrefixLength) {
                    maxPrefixLength = prefixLength;
                    melhorRota = rotaCandidata;
                }
            }
        }
        
        // Se a rota default (0.0.0.0/0) estiver cadastrada, ela será retornada se nenhuma outra for encontrada.
        return melhorRota;
    }
}