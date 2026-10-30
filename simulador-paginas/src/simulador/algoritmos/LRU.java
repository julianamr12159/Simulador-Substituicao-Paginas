package simulador.algoritmos;

import java.util.ArrayList;
import java.util.List;

public class LRU implements AlgoritmoSubstituicao {

    @Override
    public String getNome() { return "LRU"; }

    @Override
    public Resultado simular(int[] referencias, int numQuadros) {
        Util.validar(referencias, numQuadros);
        int[] quadros = Util.quadrosVazios(numQuadros);
        int[] ultimoAcesso = new int[numQuadros];   // instante do último uso de cada quadro
        int ocupados = 0;
        List<Passo> passos = new ArrayList<>();

        for (int tempo = 0; tempo < referencias.length; tempo++) {
            int pagina = referencias[tempo];
            int pos = Util.buscar(quadros, pagina);

            if (pos != -1) {                         // acerto: só atualiza o timestamp
                ultimoAcesso[pos] = tempo;
                passos.add(new Passo(pagina, quadros, false, -1));
                continue;
            }
            int removida = -1;
            if (ocupados < numQuadros) {
                pos = ocupados++;
            } else {                                 // procura o menos recentemente usado
                pos = 0;
                for (int i = 1; i < numQuadros; i++) {
                    if (ultimoAcesso[i] < ultimoAcesso[pos]) pos = i;
                }
                removida = quadros[pos];
            }
            quadros[pos] = pagina;
            ultimoAcesso[pos] = tempo;
            passos.add(new Passo(pagina, quadros, true, removida));
        }
        return new Resultado(getNome(), numQuadros, passos);
    }
}
