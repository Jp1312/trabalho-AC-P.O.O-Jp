//Joao Pedro Bezamat, matricula 202301216109, programação O.O

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Loja loja = new Loja();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Alterar produto");
            System.out.println("3. Registrar venda");
            System.out.println("4. Emitir relatório");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            if (opcao == 1) {
                System.out.print("Código: ");
                int codigo = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha
                System.out.print("Descrição: ");
                String descricao = scanner.nextLine();
                System.out.print("Preço: ");
                double preco = scanner.nextDouble();
                System.out.print("Quantidade: ");
                int quantidade = scanner.nextInt();

                Produto produto = new Produto(codigo, descricao, preco, quantidade);
                boolean cadastrado = loja.cadastrarProduto(produto);
                if (cadastrado) {
                    System.out.println("Produto cadastrado com sucesso!");
                } else {
                    System.out.println("Produto com esse código já existe!");
                }

            } else if (opcao == 2) {
                System.out.print("Código do produto a alterar: ");
                int codigo = scanner.nextInt();
                scanner.nextLine();
                Produto produto = loja.buscarProduto(codigo);
                if (produto == null) {
                    System.out.println("Produto não encontrado!");
                    continue;
                }
                System.out.print("Nova descrição: ");
                String novaDescricao = scanner.nextLine();
                System.out.print("Novo preço: ");
                double novoPreco = scanner.nextDouble();
                System.out.print("Nova quantidade: ");
                int novaQuantidade = scanner.nextInt();

                boolean alterado = loja.alterarProduto(codigo, novaDescricao, novoPreco, novaQuantidade);
                if (alterado) {
                    System.out.println("Produto alterado com sucesso!");
                } else {
                    System.out.println("Erro ao alterar produto!");
                }

            } else if (opcao == 3) {
                scanner.nextLine();
                System.out.print("Nome do cliente: ");
                String nomeCliente = scanner.nextLine();
                Venda venda = new Venda(nomeCliente);

                while (true) {
                    System.out.print("Código do produto (ou 0 para finalizar): ");
                    int codigoProduto = scanner.nextInt();
                    if (codigoProduto == 0) {
                        break;
                    }
                    System.out.print("Quantidade: ");
                    int quantidade = scanner.nextInt();

                    Produto produto = loja.buscarProduto(codigoProduto);
                    if (produto == null) {
                        System.out.println("Produto não encontrado!");
                        continue;
                    }
                    if (produto.getQuantidade() < quantidade) {
                        System.out.println("Estoque insuficiente!");
                        continue;
                    }

                    venda.adicionarItem(codigoProduto, quantidade);
                }

                boolean registrada = loja.registrarVenda(venda);
                if (registrada) {
                    double total = venda.calcularTotal(loja.getProdutos());
                    System.out.println("Venda registrada com sucesso!");
                    System.out.printf("Total da venda: R$ %.2f%n", total);
                } else {
                    System.out.println("Erro ao registrar venda!");
                }

            } else if (opcao == 4) {
                scanner.nextLine(); // Consumir a nova linha
                System.out.println("1. Inventário");
                System.out.println("2. Relatório de vendas do dia");
                System.out.println("3. Relatório de vendas geral");
                System.out.print("Escolha o relatório: ");
                int tipoRelatorio = scanner.nextInt();
                scanner.nextLine(); // Consumir a nova linha

                System.out.println("1. Imprimir na tela");
                System.out.println("2. Salvar em arquivo");
                System.out.print("Escolha o formato: ");
                int formatoRelatorio = scanner.nextInt();
                scanner.nextLine();
                PrintWriter writer = null;
                if (formatoRelatorio == 1) {
                    writer = new PrintWriter(System.out);
                } else if (formatoRelatorio == 2) {
                    try {
                        writer = new PrintWriter(new FileWriter("relatorio.txt"));
                    } catch (IOException e) {
                        System.out.println("Erro ao criar o arquivo de relatório: " + e.getMessage());
                        continue;
                    }
                }

                if (writer != null) {
                    if (tipoRelatorio == 1) {
                        loja.gerarRelatorioInventario(writer);
                    } else if (tipoRelatorio == 2) {
                        System.out.print("Digite a data (dd/MM/yyyy): ");
                        String data = scanner.nextLine();
                        loja.gerarRelatorioVendasDoDia(writer, data);
                    } else if (tipoRelatorio == 3) {
                        loja.gerarRelatorioVendasGeral(writer);
                    }

                    if (formatoRelatorio == 2) {
                        writer.close();
                        System.out.println("Relatório salvo em relatorio.txt");
                    }
                }

            } else if (opcao == 5) {
                System.out.println("Saindo...");
                break;
            } else {
                System.out.println("Opção inválida!");
            }
        }

        scanner.close();
    }
}