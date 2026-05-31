# UVA 929 - Number Maze

**Problema:** Number Maze

**Link:** https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=870

**Grupo D**

## Integrantes

- Bianca Oriá, Guilherme Garcia e Pedro Angelus

## Linguagem

Java

## Como executar

### Pré-requisitos

- JDK 8 ou superior instalado

### Compilação

```bash
javac src/Main.java
```

### Execução

**Linux/macOS ou Windows (cmd):**
```bash
java -cp src Main < dados/entrada_do_problema.txt
```

**Windows (PowerShell):**
```powershell
Get-Content dados/entrada_do_problema.txt | java -cp src Main
```

## Modelagem do problema

### Vértices

Cada célula **(i, j)** da grade N×M é mapeada para um vértice com índice **i * M + j**. O total de vértices é **N * M**.

### Arestas

Cada vértice possui até 4 arestas direcionadas, uma para cada vizinho ortogonal (norte, sul, leste, oeste), desde que o vizinho esteja dentro dos limites da grade.

### Pesos

O peso de uma aresta que vai de **(i, j)** para **(ni, nj)** é o valor da célula **destino** **labyrinth[ni][nj]**. O custo total do caminho inclui o valor da célula de origem **(0, 0)**, somado ao acumulado retornado pelo Dijkstra.

### Origem e destino

- Origem: célula **(0, 0)**, índice **0**
- Destino: célula **(N-1, M-1)**, índice **N*M - 1**

### Representação adotada

A grade é representada como uma matriz bidimensional **int[][]**. O grafo é implícito: as arestas são geradas dinamicamente durante a exploração, com base nos quatro vizinhos ortogonais. Não há lista de adjacência explícita — as transições são computadas a partir da posição atual.

### Por que Dijkstra é aplicável

Os valores das células estão entre 0 e 9 (pesos não negativos), o que garante a corretude do algoritmo de Dijkstra.

## Algoritmo utilizado

Dijkstra de origem única com fila de prioridade indexada (**IndexMinPQ**).

## Variação de Dijkstra

Dijkstra padrão aplicado sobre grade ponderada modelada como grafo implícito. Não há variação especial além da conversão de índice bidimensional para unidimensional (**vértice = linha * colunas + coluna**).

## Estratégia

1. Inicializar todas as distâncias como infinito.
2. Definir **distância[0] = 0** e inserir o vértice **0** na fila de prioridade mínima.
3. A cada iteração, extrair o vértice de menor distância acumulada.
4. Para cada vizinho ortogonal válido, tentar relaxar a aresta: se **dist[atual] + peso(vizinho) < dist[vizinho]**, atualizar e reinserir/decrementar na fila.
5. Ao término, **distância[N*M - 1]** contém o menor custo até o destino (excluindo a origem).
6. O resultado final é **distância[N*M - 1] + labyrinth[0][0]**.

## Análise de complexidade

Sendo **V = N * M** o número de vértices e **E = O(V)** o número de arestas (cada célula tem no máximo 4 vizinhos):

- **Tempo:** **O(V log V)** — cada vértice é inserido e removido da fila uma vez, e cada aresta pode provocar um **decreaseKey**.
- **Espaço:** **O(V)** — vetor de distâncias, fila de prioridade indexada e matriz da grade.

A estrutura dominante de memória é a própria grade **N × M**, com **N, M ≤ 999**, totalizando até ~10⁶ células.

## Casos especiais

- **Destino inalcançável:** não ocorre neste problema, pois a grade é sempre conexa ortogonalmente.
- **Grade 1×1:** origem e destino são a mesma célula; o resultado é o valor único da grade.
- **Múltiplos casos de teste:** o programa lê o número de casos na primeira linha e executa o Dijkstra independentemente para cada um.
- **Pesos iguais a zero:** permitidos pelo enunciado (valores entre 0 e 9); o algoritmo trata corretamente.

## Evidência de submissão aceita

![Accepted](evidencias/Captura%20de%20tela%202026-05-27%20085823.png)

PDF da mensagem recebida por Gmail:  [**evidencias/Submission 31151924 - Accepted - Gmail.pdf**](evidencias/Submission%2031151924%20-%20Accepted%20-%20Gmail.pdf)

PDF da submissão feita no site: [**evidencias/Number-Maze.pdf**](evidencias/Number-Maze.pdf)