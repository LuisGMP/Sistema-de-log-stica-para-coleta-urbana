import java.util.List;

/**
 * Funcionalidade: "Comparar custo da rota atual com a rota otimizada".
 * Dada uma rota atual (definida manualmente, ex.: a que o motorista já faz)
 * e um par origem/destino, calcula:
 *   1. O custo da rota atual (somando as arestas percorridas).
 *   2. O custo da rota otimizada pelo Dijkstra entre a mesma origem e destino.
 *   3. A economia (absoluta e percentual) obtida ao usar a rota otimizada.
 */
public class ComparadorRotas {

    /** Objeto de resultado, útil para exibir na tela ou logar. */
    public static class ResultadoComparacao {
        public final Rota rotaAtual;
        public final double custoAtual;
        public final List<String> rotaOtimizada;
        public final double custoOtimizado;

        ResultadoComparacao(Rota rotaAtual, double custoAtual, List<String> rotaOtimizada, double custoOtimizado) {
            this.rotaAtual = rotaAtual;
            this.custoAtual = custoAtual;
            this.rotaOtimizada = rotaOtimizada;
            this.custoOtimizado = custoOtimizado;
        }

        public double getEconomia() {
            return custoAtual - custoOtimizado;
        }

        public double getEconomiaPercentual() {
            if (custoAtual == 0) return 0;
            return (getEconomia() / custoAtual) * 100.0;
        }

        public void imprimir() {
            System.out.println("Rota atual:      " + rotaAtual);
            System.out.println("Custo atual:      " + formatar(custoAtual));
            System.out.println("Rota otimizada:  " + String.join(" -> ", rotaOtimizada));
            System.out.println("Custo otimizado:  " + formatar(custoOtimizado));

            double economia = getEconomia();
            if (economia > 0) {
                System.out.printf("Economia:         %s (%.1f%% mais barata)%n", formatar(economia), getEconomiaPercentual());
            } else if (economia < 0) {
                System.out.println("A rota atual já é ótima (o Dijkstra não encontrou nada melhor).");
            } else {
                System.out.println("A rota atual já é a rota de menor custo possível.");
            }
        }

        private String formatar(double valor) {
            return String.format("%.2f", valor);
        }
    }

    public static ResultadoComparacao comparar(Grafo grafo, Rota rotaAtual, String origem, String destino) {
        double custoAtual = rotaAtual.calcularCusto(grafo);

        ResultadoDijkstra resultado = Dijkstra.calcular(grafo, origem);
        if (!resultado.isAlcancavel(destino)) {
            throw new IllegalStateException("Destino \"" + destino + "\" não é alcançável a partir de \"" + origem + "\".");
        }

        List<String> caminhoOtimizado = resultado.reconstruirCaminho(destino);
        double custoOtimizado = resultado.getDistancia(destino);

        return new ResultadoComparacao(rotaAtual, custoAtual, caminhoOtimizado, custoOtimizado);
    }
}
