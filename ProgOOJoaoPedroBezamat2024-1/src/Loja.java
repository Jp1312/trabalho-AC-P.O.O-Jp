import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;


//classe loja+ METODOS
public class Loja {
    private static final String FILE_NAME = "cadastro.txt";
    private static final String VENDAS_FILE_NAME = "vendas.txt";
    private List<Produto> produtos;

    public Loja() {
        this.produtos = carregarProdutos();
    }

    private List<Produto> carregarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                int codigo = Integer.parseInt(partes[0]);
                String descricao = partes[1];
                double preco = Double.parseDouble(partes[2]);
                int quantidade = Integer.parseInt(partes[3]);
                produtos.add(new Produto(codigo, descricao, preco, quantidade));
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar produtos: " + e.getMessage());
        }
        return produtos;
    }

    private void salvarProdutos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Produto produto : produtos) {
                pw.println(produto);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }

    public boolean cadastrarProduto(Produto produto) {
        for (Produto p : produtos) {
            if (p.getCodigo() == produto.getCodigo()) {
                return false;
            }
        }
        produtos.add(produto);
        salvarProdutos();
        return true;
    }

    public Produto buscarProduto(int codigo) {
        for (Produto p : produtos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }

    public boolean alterarProduto(int codigo, String novaDescricao, double novoPreco, int novaQuantidade) {
        Produto produto = buscarProduto(codigo);
        if (produto == null) {
            return false;
        }
        produto.setDescricao(novaDescricao);
        produto.setPreco(novoPreco);
        produto.setQuantidade(novaQuantidade);
        salvarProdutos();
        return true;
    }

    public boolean registrarVenda(Venda venda) {
        for (ItemVenda item : venda.getItens()) {
            Produto produto = buscarProduto(item.getCodigoProduto());
            if (produto == null || produto.getQuantidade() < item.getQuantidade()) {
                return false;
            }
        }
        for (ItemVenda item : venda.getItens()) {
            Produto produto = buscarProduto(item.getCodigoProduto());
            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
        }
        salvarProdutos();
        salvarVenda(venda);
        return true;
    }

    private void salvarVenda(Venda venda) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VENDAS_FILE_NAME, true))) { // Adiciona no final do arquivo
            pw.println(venda);
        } catch (IOException e) {
            System.out.println("Erro ao salvar venda: " + e.getMessage());
        }
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void gerarRelatorioInventario(PrintWriter writer) {
        writer.println("Relatório de Inventário:");
        for (Produto produto : produtos) {
            writer.println(produto.getCodigo() + " - " + produto.getDescricao() + " - Quantidade: " + produto.getQuantidade());
        }
        writer.flush();
    }

    public void gerarRelatorioVendasDoDia(PrintWriter writer, String data) {
        writer.println("Relatório de Vendas do Dia (" + data + "):");
        try (BufferedReader br = new BufferedReader(new FileReader(VENDAS_FILE_NAME))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.startsWith(data)) {
                    writer.println(linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de vendas: " + e.getMessage());
        }
        writer.flush();
    }

    public void gerarRelatorioVendasGeral(PrintWriter writer) {
        writer.println("Relatório de Vendas Geral:");
        try (BufferedReader br = new BufferedReader(new FileReader(VENDAS_FILE_NAME))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                writer.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de vendas: " + e.getMessage());
        }
        writer.flush();
    }
}
