package simulador.algoritmos;

import java.util.ArrayList;
import java.util.List;

public class Relogio implements AlgoritmoSubstituicao {

    @Override
    public String getNome() { return "Relógio"; }

    @Override
    public Resultado simular(int[] referencias, int numQuadros) {
        Util.validar(referencias, numQuadros);
        int[] quadros = Util.quadrosVazios(numQuadros);
        int[] bitR = new int[numQuadros];
        int ponteiro = 0;
        List<Passo> passos = new ArrayList<>();

        for (int pagina : referencias) {
            int pos = Util.buscar(quadros, pagina);

            if (pos != -1) {                                   // acerto: marca como usada
                bitR[pos] = 1;
                passos.add(new Passo(pagina, quadros, false, -1, bitR, ponteiro));
                continue;
            }
            // Falta de página: gira o ponteiro até achar um quadro vazio ou com R = 0
            while (quadros[ponteiro] != -1 && bitR[ponteiro] == 1) {
                bitR[ponteiro] = 0;                            // segunda chance
                ponteiro = (ponteiro + 1) % numQuadros;
            }
            int removida = quadros[ponteiro];                  // -1 se o quadro estava vazio
            quadros[ponteiro] = pagina;
            bitR[ponteiro] = 1;
            ponteiro = (ponteiro + 1) % numQuadros;
            passos.add(new Passo(pagina, quadros, true, removida, bitR, ponteiro));
        }
        return new Resultado(getNome(), numQuadros, passos);
    }
}
