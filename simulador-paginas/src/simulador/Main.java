package simulador;

import simulador.algoritmos.Passo;
import simulador.algoritmos.Resultado;
import simulador.gui.JanelaPrincipal;

import java.awt.GraphicsEnvironment;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean console = false, passos = false;
        String cadeia = null;
        Integer quadros = null;

        for (String a : args) {
            if (a.equalsIgnoreCase("--console") || a.equalsIgnoreCase("-c")) console = true;
            else if (a.equalsIgnoreCase("--passos") || a.equalsIgnoreCase("-p")) passos = true;
            else if (cadeia == null) cadeia = a;
            else if (quadros == null) quadros = Integer.parseInt(a.trim());
        }

        if (!console && !GraphicsEnvironment.isHeadless()) {
           JanelaPrincipal.abrir();
           return;
        }
        executarConsole(cadeia, quadros, passos);
    }

    private static void executarConsole(String cadeiaTexto, Integer quadros, boolean mostrarPassos) {
        PrintStream out = System.out;
        Scanner sc = new Scanner(System.in);

        out.println("=== Simulador de Algoritmos de Substituição de Páginas ===");
        if (cadeiaTexto == null) {
            out.print("Cadeia de referências (Enter = exemplo \"" + Simulador.CADEIA_EXEMPLO + "\"): ");
            String linha = sc.hasNextLine() ? sc.nextLine() : "";
            cadeiaTexto = linha.isBlank() ? Simulador.CADEIA_EXEMPLO : linha;
        }
        if (quadros == null) {
            out.print("Número de quadros (Enter = " + Simulador.QUADROS_EXEMPLO + "): ");
            String linha = sc.hasNextLine() ? sc.nextLine() : "";
            quadros = linha.isBlank() ? Simulador.QUADROS_EXEMPLO : Integer.parseInt(linha.trim());
        }

        int[] cadeia;
        try {
            cadeia = Simulador.lerCadeia(cadeiaTexto);
        } catch (IllegalArgumentException e) {
            out.println("Erro: " + e.getMessage());
            return;
        }

        out.println();
        out.println("Cadeia (" + cadeia.length + " referências): " + Simulador.formatar(cadeia));
        out.println("Quadros: " + quadros);
        out.println();

        List<Resultado> resultados = Simulador.simularTodos(cadeia, quadros);
        int n = 1;
        for (Resultado r : resultados) {
            out.printf("- Método %d (%s) - %d faltas de página%n", n++, r.getAlgoritmo(), r.getFaltas());
        }

        if (mostrarPassos) {
            for (Resultado r : resultados) imprimirPassos(out, r);
        }
    }

    private static void imprimirPassos(PrintStream out, Resultado r) {
        out.println();
        out.println("--- " + r.getAlgoritmo() + " (passo a passo) ---");
        int t = 1;
        for (Passo p : r.getPassos()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < p.getQuadros().length; i++) {
                int q = p.getQuadros()[i];
                sb.append(q == -1 ? "  -" : String.format("%3d", q));
                if (p.getBitsR() != null && q != -1) sb.append("(").append(p.getBitsR()[i]).append(")");
                else if (p.getBitsR() != null) sb.append("   ");
            }
            String info = p.isFalta() ? "FALTA" + (p.getSubstituida() >= 0 ? " (sai " + p.getSubstituida() + ")" : "") : "acerto";
            out.printf("%3d | pág %3d | %s | %s%n", t++, p.getPagina(), sb, info);
        }
    }
}
