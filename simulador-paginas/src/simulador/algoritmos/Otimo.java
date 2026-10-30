package simulador.algoritmos;

import java.util.ArrayList;
import java.util.List;

public class Otimo implements AlgoritmoSubstituicao {

    @Override
    public String getNome() { return "Ótimo"; }

    @Override
    public Resultado simular(int[] referencias, int numQuadros) {
        Util.validar(referencias, numQuadros);
        int[] quadros = Util.quadrosVazios(numQuadros);
        int ocupados = 0;
        List<Passo> passos = new ArrayList<>();

        for (int tempo = 0; tempo < referencias.length; tempo++) {
            int pagina = referencias[tempo];

            if (Util.buscar(quadros, pagina) != -1) {
                passos.add(new Passo(pagina, quadros, false, -1));
                continue;
            }
            int removida = -1;
            int pos;
            if (ocupados < numQuadros) {
                pos = ocupados++;
            } else {
                // Escolhe o quadro cuja página tem o próximo uso mais distante
                pos = 0;
                int maisDistante = -1;
                for (int i = 0; i < numQuadros; i++) {
                    int proximo = proximoUso(referencias, tempo + 1, quadros[i]);
                    if (proximo > maisDistante) {
                        maisDistante = proximo;
                        pos = i;
                        if (proximo == Integer.MAX_VALUE) break;   // nunca mais usada: escolha imediata
                    }
                }
                removida = quadros[pos];
            }
            quadros[pos] = pagina;
            passos.add(new Passo(pagina, quadros, true, removida));
        }
        return new Resultado(getNome(), numQuadros, passos);
    }

    // Índice da próxima referência à página a partir de 'inicio'
    private static int proximoUso(int[] referencias, int inicio, int pagina) {
        for (int j = inicio; j < referencias.length; j++) {
            if (referencias[j] == pagina) return j;
        }
        return Integer.MAX_VALUE;   // se não houver
    }
}
