package simulador.gui;

import simulador.algoritmos.Resultado;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class GraficoBarras extends JPanel {

    private List<Resultado> resultados = new ArrayList<>();
    private String subtitulo = "";

    public GraficoBarras() {
        setBackground(Cores.CARTAO);
        setPreferredSize(new Dimension(760, 380));
    }

    public void setDados(List<Resultado> resultados, String subtitulo) {
        this.resultados = resultados;
        this.subtitulo = subtitulo;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        desenhar((Graphics2D) g0, getWidth(), getHeight());
    }

    void desenhar(Graphics2D g, int w, int h) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Cores.CARTAO);
        g.fillRect(0, 0, w, h);

        g.setColor(Cores.TEXTO);
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.drawString("Faltas de página por algoritmo", 20, 28);
        g.setColor(Cores.TEXTO_SEC);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.drawString(subtitulo, 20, 46);

        if (resultados.isEmpty()) {
            g.drawString("Clique em \"Simular\" para gerar o gráfico.", 20, h / 2);
            return;
        }

        int esq = 56, dir = 24, topo = 70, base = 50;
        int areaW = w - esq - dir, areaH = h - topo - base;
        int max = resultados.stream().mapToInt(Resultado::getFaltas).max().orElse(1);
        int escala = escalaBonita(max);

        // grade horizontal + rótulos do eixo Y
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int passo = Math.max(1, escala / 5);
        for (int v = 0; v <= escala; v += passo) {
            int y = topo + areaH - (int) Math.round((double) v / escala * areaH);
            g.setColor(v == 0 ? Cores.TEXTO_SEC : Cores.GRADE);
            g.drawLine(esq, y, esq + areaW, y);
            g.setColor(Cores.TEXTO_SEC);
            String s = String.valueOf(v);
            g.drawString(s, esq - 8 - g.getFontMetrics().stringWidth(s), y + 4);
        }

        int n = resultados.size();
        int fatia = areaW / n;
        int larg = Math.min(90, (int) (fatia * 0.55));
        for (int i = 0; i < n; i++) {
            Resultado r = resultados.get(i);
            int alt = (int) Math.round((double) r.getFaltas() / escala * areaH);
            int x = esq + i * fatia + (fatia - larg) / 2;
            int y = topo + areaH - alt;

            g.setColor(Cores.doAlgoritmo(i));
            g.fillRoundRect(x, y, larg, alt + 4, 6, 6);
            g.fillRect(x, y + 4, larg, Math.max(0, alt - 4));   // base reta

            g.setColor(Cores.TEXTO);
            g.setFont(new Font("SansSerif", Font.BOLD, 13));
            String val = String.valueOf(r.getFaltas());
            g.drawString(val, x + (larg - g.getFontMetrics().stringWidth(val)) / 2, y - 6);

            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            String nome = r.getAlgoritmo();
            g.drawString(nome, x + (larg - g.getFontMetrics().stringWidth(nome)) / 2, topo + areaH + 18);
            g.setColor(Cores.TEXTO_SEC);
            String taxa = String.format("%.1f%%", r.getTaxaFaltas());
            g.drawString(taxa, x + (larg - g.getFontMetrics().stringWidth(taxa)) / 2, topo + areaH + 34);
        }
    }

    // Arredonda o número o máximo para cima para o eixo
    static int escalaBonita(int max) {
        if (max <= 5) return Math.max(max, 1);
        int[] passos = {1, 2, 5};
        int mag = 1;
        while (true) {
            for (int p : passos) {
                int s = p * mag;
                if (s * 5 >= max) return ((max + s - 1) / s) * s;
            }
            mag *= 10;
        }
    }

    public void salvarPng(File arquivo, int w, int h) throws IOException {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        desenhar(g, w, h);
        g.dispose();
        ImageIO.write(img, "png", arquivo);
    }
}
