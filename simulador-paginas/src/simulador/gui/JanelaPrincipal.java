package simulador.gui;

import simulador.Simulador;
import simulador.algoritmos.Resultado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JanelaPrincipal extends JFrame {

    private static final String CADEIA_BELADY = "1 2 3 4 1 2 5 1 2 3 4 5";

    private final JTextField campoCadeia = new JTextField(Simulador.CADEIA_EXEMPLO, 40);
    private final JSpinner campoQuadros = new JSpinner(new SpinnerNumberModel(Simulador.QUADROS_EXEMPLO, 1, 50, 1));
    private final JLabel status = new JLabel(" ");
    private final JPanel painelCartoes = new JPanel(new GridLayout(1, 4, 12, 0));
    private final GraficoBarras graficoBarras = new GraficoBarras();

    private List<Resultado> resultados = new ArrayList<>();

    public static void abrir() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        SwingUtilities.invokeLater(() -> {
            JanelaPrincipal j = new JanelaPrincipal();
            j.setVisible(true);
        });
    }

    public JanelaPrincipal() {
        super("Simulador de Algoritmos de Substituição de Páginas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 640));
        setSize(1100, 760);
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout(0, 12));
        raiz.setBackground(Cores.FUNDO);
        raiz.setBorder(new EmptyBorder(16, 16, 16, 16));
        raiz.add(criarCabecalho(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setOpaque(false);
        painelCartoes.setOpaque(false);
        centro.add(painelCartoes, BorderLayout.NORTH);
        centro.add(criarAbas(), BorderLayout.CENTER);
        raiz.add(centro, BorderLayout.CENTER);

        setContentPane(raiz);
        simular();
    }

    // montagem da tela

    private JComponent criarCabecalho() {
        JPanel p = cartao(new BorderLayout(0, 10));

        JLabel titulo = new JLabel("Simulador de Substituição de Páginas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Cores.TEXTO);
        JLabel sub = new JLabel("FIFO · LRU · Relógio (Segunda Chance) · Ótimo");
        sub.setForeground(Cores.TEXTO_SEC);
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(sub);
        p.add(textos, BorderLayout.NORTH);

        JPanel entrada = new JPanel(new GridBagLayout());
        entrada.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 0, 2, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        entrada.add(rotulo("Cadeia de referências (páginas)"), c);
        c.gridx = 1;
        entrada.add(rotulo("Quadros"), c);

        c.gridy = 1; c.gridx = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        campoCadeia.setFont(new Font("Monospaced", Font.PLAIN, 14));
        campoCadeia.setToolTipText("Números inteiros separados por espaço ou vírgula. Enter simula.");
        campoCadeia.addActionListener(e -> simular());
        entrada.add(campoCadeia, c);

        c.gridx = 1; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        ((JSpinner.DefaultEditor) campoQuadros.getEditor()).getTextField().setColumns(3);
        campoQuadros.addChangeListener(e -> simular());
        entrada.add(campoQuadros, c);

        JButton btSimular = new JButton("Simular");
        btSimular.setFont(btSimular.getFont().deriveFont(Font.BOLD));
        btSimular.addActionListener(e -> simular());
        c.gridx = 2;
        entrada.add(btSimular, c);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        botoes.setOpaque(false);
        botoes.add(botao("Exemplo", () -> carregar(Simulador.CADEIA_EXEMPLO, 3)));
        botoes.add(Box.createHorizontalStrut(6));
        botoes.add(botao("Anomalia de Belady", () -> carregar(CADEIA_BELADY, 4)));
        botoes.add(Box.createHorizontalStrut(6));
        botoes.add(botao("Cadeia aleatória", this::gerarAleatoria));
        botoes.add(Box.createHorizontalStrut(6));
        c.gridx = 0; c.gridy = 2; c.gridwidth = 3; c.insets = new Insets(8, 0, 0, 0);
        entrada.add(botoes, c);

        c.gridy = 3; c.insets = new Insets(6, 0, 0, 0);
        status.setForeground(Cores.FALTA_TXT);
        entrada.add(status, c);

        p.add(entrada, BorderLayout.CENTER);
        return p;
    }

    private JComponent criarAbas() {
        return envolver(graficoBarras);
    }

    // ações

    private void simular() {
        int[] cadeia;
        try {
            cadeia = Simulador.lerCadeia(campoCadeia.getText());
        } catch (IllegalArgumentException e) {
            status.setText(e.getMessage());
            return;
        }
        status.setText(" ");
        int quadros = (Integer) campoQuadros.getValue();
        resultados = Simulador.simularTodos(cadeia, quadros);

        atualizarCartoes();
        graficoBarras.setDados(resultados, cadeia.length + " referências · " + quadros + " quadros");
    }

    private void atualizarCartoes() {
        painelCartoes.removeAll();
        int i = 0;
        for (Resultado r : resultados) {
            painelCartoes.add(cartaoResultado(i + 1, r, Cores.doAlgoritmo(i)));
            i++;
        }
        painelCartoes.revalidate();
        painelCartoes.repaint();
    }

    private void carregar(String cadeia, int quadros) {
        campoCadeia.setText(cadeia);
        campoQuadros.setValue(quadros);   // dispara simular() se o valor mudar
        simular();
    }

    private void gerarAleatoria() {
        int[] c = Simulador.gerarAleatoria(24, 9);
        carregar(Simulador.formatar(c), (Integer) campoQuadros.getValue());
    }

    // componentes auxiliares

    private static JPanel cartao(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(Cores.CARTAO);
        p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Cores.BORDA), new EmptyBorder(14, 16, 14, 16)));
        return p;
    }

    private static JComponent envolver(JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.CARTAO);
        p.add(c);
        return p;
    }

    private static JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Cores.TEXTO_SEC);
        return l;
    }

    private static JButton botao(String texto, Runnable acao) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> acao.run());
        return b;
    }

    private static JComponent cartaoResultado(int numero, Resultado r, Color cor) {
        JPanel p = new JPanel(new BorderLayout(0, 4)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(cor);
                g.fillRect(0, 0, 5, getHeight());
            }
        };
        p.setBackground(Cores.CARTAO);
        p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Cores.BORDA), new EmptyBorder(10, 16, 10, 12)));

        JLabel nome = new JLabel("Método " + numero + " · " + r.getAlgoritmo());
        nome.setForeground(Cores.TEXTO_SEC);
        JLabel valor = new JLabel(r.getFaltas() + " faltas de página");
        valor.setFont(new Font("SansSerif", Font.BOLD, 18));
        valor.setForeground(Cores.TEXTO);
        JLabel extra = new JLabel(String.format("%d acertos · %.1f%% de faltas", r.getAcertos(), r.getTaxaFaltas()));
        extra.setForeground(Cores.TEXTO_SEC);

        p.add(nome, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        p.add(extra, BorderLayout.SOUTH);
        return p;
    }
}
