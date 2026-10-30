package simulador;

import simulador.algoritmos.AlgoritmoSubstituicao;
import simulador.algoritmos.FIFO;
import simulador.algoritmos.LRU;
import simulador.algoritmos.Otimo;
import simulador.algoritmos.Relogio;
import simulador.algoritmos.Resultado;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Simulador {

    // Cadeia usada como exemplo padrão
    public static final String CADEIA_EXEMPLO = "7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1";
    public static final int QUADROS_EXEMPLO = 3;

    private Simulador() { }

    public static List<AlgoritmoSubstituicao> algoritmos() {
        return List.of(new FIFO(), new LRU(), new Relogio(), new Otimo());
    }

    public static List<Resultado> simularTodos(int[] referencias, int numQuadros) {
        List<Resultado> resultados = new ArrayList<>();
        for (AlgoritmoSubstituicao alg : algoritmos()) {
            resultados.add(alg.simular(referencias, numQuadros));
        }
        return resultados;
    }

    // Converte "7 0 1, 2;0" em {7,0,1,2,0}, aceita espaço, vírgula, ponto e vírgula ou hífen como separador
    public static int[] lerCadeia(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Informe a cadeia de referências (ex.: 7 0 1 2 0 3).");
        }
        String[] partes = texto.trim().split("[\\s,;\\-]+");
        int[] cadeia = new int[partes.length];
        for (int i = 0; i < partes.length; i++) {
            try {
                cadeia[i] = Integer.parseInt(partes[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Valor inválido na cadeia: \"" + partes[i] + "\". Use apenas números inteiros.");
            }
            if (cadeia[i] < 0) {
                throw new IllegalArgumentException("Páginas não podem ser negativas: " + cadeia[i]);
            }
        }
        return cadeia;
    }

    // Gera uma cadeia aleatória com 'tamanho' referências entre 0 e maxPagina
    public static int[] gerarAleatoria(int tamanho, int maxPagina) {
        Random r = new Random();
        int[] cadeia = new int[tamanho];
        for (int i = 0; i < tamanho; i++) cadeia[i] = r.nextInt(maxPagina + 1);
        return cadeia;
    }

    public static String formatar(int[] cadeia) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cadeia.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(cadeia[i]);
        }
        return sb.toString();
    }
}
