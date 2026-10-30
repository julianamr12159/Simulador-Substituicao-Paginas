package simulador.algoritmos;

// Estado da memória depois de UMA referência da cadeia.
// Monta a tabela passo a passo na interface.

public class Passo {

    private final int pagina;        // página referenciada neste instante
    private final int[] quadros;     // conteúdo dos quadros (-1 = quadro vazio)
    private final boolean falta;     // houve falta de página?
    private final int substituida;   // página removida da memória (-1 = nenhuma)
    private final int[] bitsR;       // bits de referência (só no Relógio; null nos demais)
    private final int ponteiro;      // posição do ponteiro do relógio (-1 nos demais)

    public Passo(int pagina, int[] quadros, boolean falta, int substituida) {
        this(pagina, quadros, falta, substituida, null, -1);
    }

    public Passo(int pagina, int[] quadros, boolean falta, int substituida, int[] bitsR, int ponteiro) {
        this.pagina = pagina;
        this.quadros = quadros.clone();
        this.falta = falta;
        this.substituida = substituida;
        this.bitsR = bitsR == null ? null : bitsR.clone();
        this.ponteiro = ponteiro;
    }

    public int getPagina() { return pagina; }
    public int[] getQuadros() { return quadros; }
    public boolean isFalta() { return falta; }
    public int getSubstituida() { return substituida; }
    public int[] getBitsR() { return bitsR; }
    public int getPonteiro() { return ponteiro; }
}
