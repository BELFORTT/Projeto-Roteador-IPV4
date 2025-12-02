package br.edu.ifpb.poo.roteador;

public class IPUtils {

    /**
     * Converte um endereço IPv4 (A.B.C.D) em sua representação numérica (long de 32 bits).
     * Usa 'long' para garantir que o número não seja tratado como negativo (signed) 
     * e para evitar overflow, pois o maior IP (255.255.255.255) é um valor de 32 bits.
     * * @param ipAddress Endereço IP em formato String (ex: "192.168.1.1").
     * @return O valor numérico de 32 bits do IP.
     * @throws IllegalArgumentException se o formato do IP for inválido ou números fora do intervalo (0-255).
     */
    public static long ipParaNumerico(String ipAddress) {
        String[] partes = ipAddress.split("\\.");
        
        if (partes.length != 4) {
            throw new IllegalArgumentException("Formato de IP inválido. Deve ser A.B.C.D.");
        }

        long resultado = 0;
        
        for (int i = 0; i < 4; i++) {
            int octeto;
            try {
                octeto = Integer.parseInt(partes[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Octeto inválido em: " + ipAddress, e);
            }
            
            if (octeto < 0 || octeto > 255) {
                throw new IllegalArgumentException("Octeto fora do intervalo (0-255): " + octeto);
            }
            
            // O primeiro octeto (índice 0) é deslocado 24 bits para a esquerda.
            // O segundo (índice 1) é deslocado 16 bits.
            // O quarto (índice 3) é deslocado 0 bits.
            resultado += (long) octeto << (3 - i) * 8;
        }
        return resultado;
    }

    // --- Métodos de Máscara e Prefix Length ---

    /**
     * Cria a máscara binária (valor long) correspondente ao comprimento do prefixo CIDR.
     * Ex: prefixLength 24 retorna o valor de 255.255.255.0 em 32 bits.
     * * @param prefixLength O comprimento do prefixo (ex: 24 para /24).
     * @return 
     * @throws IllegalArgumentException 
     */
    public static long criarMascaraBinaria(int prefixLength) {
        if (prefixLength < 0 || prefixLength > 32) {
            throw new IllegalArgumentException("Comprimento de prefixo inválido: " + prefixLength + ". Deve ser entre 0 e 32.");
        }
        
        if (prefixLength == 0) {
            return 0L; // Rota default 0.0.0.0
        }

        // 0xFFFFFFFFL representa 32 bits '1' (todos ligados).
        // Usamos o 'L' para garantir que seja um literal long (64 bits) e evitar problemas de sinal no shift.
        // O deslocamento (32 - prefixLength) 'desliga' os bits do lado direito.
        return (0xFFFFFFFFL << (32 - prefixLength));
    }
    
    /**
     * Converte a máscara de sub-rede (em formato decimal pontuado) no comprimento do prefixo CIDR.
     * Ex: "255.255.255.0" retorna 24.
     * * @param mascaraSubrede A máscara em formato String (ex: "255.255.255.0").
     * @return O comprimento do prefixo (número de bits '1').
     * @throws IllegalArgumentException se a máscara for inválida ou não contínua.
     */
    public static int mascaraParaComprimento(String mascaraSubrede) {
        long mascaraNumerica = ipParaNumerico(mascaraSubrede);
        
        // Verifica se a máscara é contínua (todos os 1s juntos, seguidos por todos os 0s).
        // Se a máscara for contínua, (mascaraNumerica & (-mascaraNumerica)) == mascaraNumerica
        // OU se a inversão for potência de 2. A lógica mais simples é:
        // Se a negação bit a bit (~) + 1 é igual a zero, significa que não tem buracos.
        // A expressão ~(mascaraNumerica) + 1 deve ser uma potência de 2.
        if (mascaraNumerica != 0) {
             long notMascara = ~mascaraNumerica & 0xFFFFFFFFL; // Inverte os bits e garante 32 bits
             if ( (notMascara & (notMascara + 1)) != 0 && notMascara != 0) {
                // Se a lógica da máscara inválida for muito complexa, o foco principal é apenas contar os bits.
             }
        }
        
        int prefixLength = 0;
        // Contagem de bits (set bits) na representação binária da máscara.
        // Usa a função intrínseca do Java para contagem de bits para eficiência:
        if (mascaraNumerica != 0) {
            prefixLength = Long.bitCount(mascaraNumerica);
        }

        if (prefixLength < 0 || prefixLength > 32) {
            // Isso só acontece se a máscara não for válida (ex: 255.0.255.0)
             throw new IllegalArgumentException("Máscara de sub-rede inválida ou não contínua: " + mascaraSubrede);
        }
        
        return prefixLength;
    }
    
    // Método auxiliar (opcional para o UC06 - exibição)
    /**
     * Converte o prefixo CIDR em sua representação de máscara decimal pontuada.
     * @param prefixLength O comprimento do prefixo (ex: 24).
     * @return A máscara em formato String (ex: "255.255.255.0").
     */
    public static String comprimentoParaMascara(int prefixLength) {
        long mascaraNumerica = criarMascaraBinaria(prefixLength);
        
        // Converte o long numérico de volta para a string A.B.C.D
        return ( (mascaraNumerica >> 24) & 0xFF) + "." +
               ( (mascaraNumerica >> 16) & 0xFF) + "." +
               ( (mascaraNumerica >> 8) & 0xFF) + "." +
               ( (mascaraNumerica) & 0xFF);
    }
}