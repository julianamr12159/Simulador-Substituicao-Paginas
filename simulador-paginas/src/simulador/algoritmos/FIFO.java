package simulador.algoritmos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FIFO implements AlgoritmoSubstituicao {

    @Override
    public String getNome() { return "FIFO"; }

    @Override
    public Resultado simular(int[] referencias, int numQuadros) {
        Util.validar(referencias, numQuadros);
        int[] quadros = Util.quadrosVazios(numQuadros);
        Deque<Integer> fila = new ArrayDeque<>();   // ordem de chegada das páginas
        int ocupados = 0;
        List<Passo> passos = new ArrayList<>();

        for (int pagina : referencias) {
            if (Util.buscar(quadros, pagina) != -1) {           // acerto: FIFO não muda nada
                passos.add(new Passo(pagina, quadros, false, -1));
                continue;
            }
            int removida = -1;
            if (ocupados < numQuadros) {                          // ainda há quadro livre
                quadros[ocupados++] = pagina;
            } else {                                              // memória cheia: remove a mais antiga
                removida = fila.pollFirst();
                quadros[Util.buscar(quadros, removida)] = pagina;
            }
            fila.addLast(pagina);
            passos.add(new Passo(pagina, quadros, true, removida));
        }
        return new Resultado(getNome(), numQuadros, passos);
    }
}
