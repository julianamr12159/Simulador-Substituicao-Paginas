# Simulador de Algoritmos de Substituição de Páginas

Trabalho da disciplina **Projeto de Sistema Operacional** — Gerenciamento de Memória

O simulador recebe uma **cadeia de páginas (números inteiros)** e um **número de quadros** da memória física e calcula quantas **faltas de página** cada algoritmo gera. Foram implementados 4 algoritmos:

| Método | Algoritmo | Ideia |
|---|---|---|
| 1 | **FIFO** | Remove a página que está há mais tempo na memória (fila). |
| 2 | **LRU** | Remove a página usada há mais tempo (timestamp do último acesso). |
| 3 | **Relógio** (Segunda Chance) | Lista circular com bit R: se R = 1, zera e dá segunda chance; se R = 0, substitui. |
| 4 | **Ótimo** | Remove a página que vai demorar mais para ser usada de novo. Serve de referência (mínimo possível). |

Possui **modo console** (saída exigida no enunciado) e **interface gráfica em Swing** com gráficos comparativos (item extra).

---

## Requisitos

- **Java JDK 17 ou superior** (testado com OpenJDK 21). Para conferir: `java -version` e `javac -version`.
- Nenhuma biblioteca externa. Os gráficos são desenhados com Java2D.

## Como executar

### Opção 1: usar o `.jar` já compilado

```bash
java -jar simulador-paginas.jar
```

Abre a interface gráfica. Para o modo texto:

```bash
java -jar simulador-paginas.jar --console
```

O programa pede a cadeia e o número de quadros. Se você apertar Enter sem digitar nada, ele usa o exemplo.

Também dá para passar tudo direto na linha de comando:

```bash
java -jar simulador-paginas.jar --console "7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1" 3
```

Saída:

```
- Método 1 (FIFO) - 15 faltas de página
- Método 2 (LRU) - 12 faltas de página
- Método 3 (Relógio) - 14 faltas de página
- Método 4 (Ótimo) - 9 faltas de página
```

Acrescente `--passos` para imprimir também o estado dos quadros a cada referência.

### Opção 2: compilar a partir do código-fonte

**Windows**

```bat
compilar.bat
java -jar simulador-paginas.jar
```

**Linux / macOS**

```bash
./compilar.sh
java -jar simulador-paginas.jar
```

**Sem script**

```bash
javac -encoding UTF-8 -d out src/simulador/*.java src/simulador/algoritmos/*.java src/simulador/gui/*.java
java -cp out simulador.Main
```

**Pela IDE (Eclipse, IntelliJ, NetBeans ou VS Code):** importe a pasta `src` como código-fonte e execute a classe `simulador.Main`.

> No terminal do Windows com Java 17, se os acentos aparecerem trocados, rode `chcp 65001` antes. No Java 18 ou superior isso não é necessário.

## Formato da entrada

- **Cadeia:** números inteiros não negativos separados por espaço, vírgula, ponto e vírgula ou hífen. Exemplos: `7 0 1 2 0 3` ou `7,0,1,2,0,3`.
- **Quadros:** inteiro de 1 a 50.

## Interface gráfica

- **Campo da cadeia + quadros + Simular.** O Enter também simula, e mudar o número de quadros atualiza na hora.
- **Botões de exemplo:**
  - *Exemplo*
  - *Anomalia de Belady* (cadeia 1 2 3 4 1 2 5 1 2 3 4 5)
  - *Cadeia aleatória*
- **Cartões** com faltas, acertos e taxa de faltas de cada método.
- **Aba Comparativo:** gráfico de barras com as faltas por algoritmo.

![Grafico Exemplo](docs/grafico_exemplo.png)

![Anomalia de Belady](docs/grafico_anomalia_belady.png)

![Grafico Aleatorio](docs/grafico_aleatorio.png)

## Estrutura do projeto

```
src/simulador/
├── Main.java                  → ponto de entrada (GUI ou --console)
├── Simulador.java             → lista dos 4 métodos, leitura da cadeia, cadeia aleatória
├── algoritmos/
│   ├── AlgoritmoSubstituicao.java  → interface comum (getNome, simular)
│   ├── FIFO.java
│   ├── LRU.java
│   ├── Otimo.java
│   ├── Passo.java             → estado dos quadros em cada referência
│   ├── Relogio.java
│   ├── Resultado.java         → total de faltas + histórico
│   └── Util.java
└── gui/
    ├── JanelaPrincipal.java   → janela Swing
    ├── GraficoBarras.java     → gráfico comparativo
    └── Cores.java
```

## Decisões de implementação

- **Carga inicial:** enquanto há quadro livre, a página entra no próximo quadro vazio. Essas faltas iniciais (compulsórias) também são contadas.
- **Relógio:** a página nova entra com **R = 1** e o ponteiro avança para o quadro seguinte, como no passo a passo da Aula 12. Em um acerto, só o bit R volta a 1.
- **Ótimo:** se mais de uma página nunca mais for usada, sai a primeira encontrada nos quadros. Isso não altera o total de faltas.

## Referências

- Slides da Aula 12 — Proj. Sistema Operacional (Prof. Izequiel).
- Simulador de referência: https://sdpm-simulator.netlify.app
