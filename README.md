# Trabalho Prático 1 - Algoritmo e Estrutura de Dados 3

## Descrição
- O trabalho consiste no desenvolvimento um software que permita a representação de entidades(objetos) em registros, bem como o armazenamento em memória secundária e manipulação através de acesso sequencial.

### Base de dados selecionada
[Amazon Kindle Books Dataset 2023](https://www.kaggle.com/datasets/asaniczka/amazon-kindle-books-dataset-2023-130k-books)
  
### Atributos da base de dados
_Foram selecionados alguns atribuitos de interesse na base de dados, como:_
- Código <span style="color:green">&#x2714;</span>
- Título <span style="color:green">&#x2714;</span>
- Autor <span style="color:green">&#x2714;</span>
- Vendido por :x: 
- URL da imagem :x: 
- URL do produto :x: 
- Avaliação <span style="color:green">&#x2714;</span>
- N° de reviews :x: 
- Preço <span style="color:green">&#x2714;</span>
- Disponível no Kindle Unlimited <span style="color:green">&#x2714;</span>
- ID da categoria :x: 
- É best seller :x: 
- É escolha do editor :x: 
- É boa opção de leitura :x: 
- Data de publicação <span style="color:green">&#x2714;</span>
- Nome da(s) categoria(s) <span style="color:green">&#x2714;</span>

- **ID na base de dados &#8594; Adicionado**

## Requisitos
- Para executar o programa, é necessário ter o Java Development Kit (JDK) instalado na máquina. O código foi desenvolvido e testado utilizando o JDK versão 11.
- Para visualizar o arquivo.db em hexadecimal foi utilizado a extensão **Hex Editor** no vscode.

## Decisões de implementação
- O primeiro registro do arquivo é um int que armazena o ultimoID na base de dados, ou seja, a quantidade de livros presentes na base.
- Após isso todo registro possui a seguinte estrutura, nessa ordem:
    - Lápide: Um char que armazena se há ou não o marcador de lápide para o registro.
    - ID: Um int que armazena o id do livro.
    - Título: Uma Sring que armazena o título do livro.
    - Autor: Uma String que armazena o nome do Autor do livro.
    - Avaliação: Float que armazena a avaliação do livro (entre 1 e 5 estrelas).
    - Preço: Float que armazena o valor do livro.
    - KindleUnlimited: Boolean que armazena se o livro está ou não disponível no Kindle Unlimited.
    - Data: long que armazena o tempo em segundos desde o início do Java Epoch até a data de publicação.
    - Nome categoria: Array de String que informa o nome das categorias do livro.

## Vídeo de apresentação
 [Apresentação TP1_AEDs III](https://youtu.be/pnpVqwxPcfI) 

## Contribuições:

- Larissa Mariella da Silva Souza
- Rondinelly Martins Silva

