package simulador.algoritmos;

import java.util.Arrays;

// Funções auxiliares compartilhadas pelos algoritmos
final class Util {

    private Util() { }

    static int[] quadrosVazios(int n) {
        int[] q = new int[n];
        Arrays.fill(q, -1);
        return q;
    }

    // Retorna o índice do quadro que contém a página, ou -1 se ela não está na memória
    static int buscar(int[] quadros, int pagina) {
        for (int i = 0; i < quadros.length; i++) {
            if (quadros[i] == pagina) return i;
        }
        return -1;
    }

    static void validar(int[] referencias, int numQuadros) {
        if (referencias == null || referencias.length == 0) {
            throw new IllegalArgumentException("A cadeia de referências está vazia.");
        }
        if (numQuadros < 1) {
            throw new IllegalArgumentException("O número de quadros deve ser pelo menos 1.");
        }
    }
}
