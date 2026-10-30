package simulador.gui;

import java.awt.Color;

public final class Cores {

    private Cores() { }

    public static final Color FUNDO      = new Color(0xF5F6F8);
    public static final Color CARTAO     = Color.WHITE;
    public static final Color BORDA      = new Color(0xDADDE3);
    public static final Color TEXTO      = new Color(0x1F2430);
    public static final Color TEXTO_SEC  = new Color(0x667085);
    public static final Color GRADE      = new Color(0xE7E9EE);
    public static final Color FALTA      = new Color(0xFDE7E7);
    public static final Color FALTA_TXT  = new Color(0xB42318);
    public static final Color ACERTO     = new Color(0xE6F4EA);
    public static final Color ACERTO_TXT = new Color(0x1E7B3A);
    public static final Color NOVA       = new Color(0xFFF4D6);

    private static final Color[] ALGORITMOS = {
        new Color(0x2F6DB5),   // FIFO
        new Color(0xE07B24),   // LRU
        new Color(0x2E9E6B),   // Relógio
        new Color(0x8A4FC7),   // Ótimo
    };

    public static Color doAlgoritmo(int indice) {
        return ALGORITMOS[indice % ALGORITMOS.length];
    }
}
