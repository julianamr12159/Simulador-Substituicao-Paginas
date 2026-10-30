package simulador.algoritmos;

import java.util.Collections;
import java.util.List;

// Resultado da simulação de um algoritmo: total de faltas + histórico passo a passo.
public class Resultado {

    private final String algoritmo;
    private final int quadros;
    private final List<Passo> passos;
    private final int faltas;

    public Resultado(String algoritmo, int quadros, List<Passo> passos) {
        this.algoritmo = algoritmo;
        this.quadros = quadros;
        this.passos = Collections.unmodifiableList(passos);
        this.faltas = (int) passos.stream().filter(Passo::isFalta).count();
    }

    public String getAlgoritmo() { return algoritmo; }
    public int getQuadros() { return quadros; }
    public List<Passo> getPassos() { return passos; }
    public int getFaltas() { return faltas; }
    public int getAcertos() { return passos.size() - faltas; }

    // Taxa de faltas em % (faltas / total de referências)
    public double getTaxaFaltas() {
        return passos.isEmpty() ? 0 : 100.0 * faltas / passos.size();
    }
}
