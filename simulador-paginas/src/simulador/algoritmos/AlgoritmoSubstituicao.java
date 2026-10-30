package simulador.algoritmos;

/**
 * Contrato comum a todos os algoritmos de substituição de páginas.
 */
public interface AlgoritmoSubstituicao {

    String getNome();

    /**
     * @param referencias cadeia de páginas (int) a serem acessadas, em ordem
     * @param numQuadros  quantidade de quadros (frames) da memória física
     * @return resultado com o total de faltas e o histórico passo a passo
     */
    Resultado simular(int[] referencias, int numQuadros);
}
