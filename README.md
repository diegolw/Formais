Regular Language Handler
-----------
Aplicação para manipular Linguagens  Regulares, expressas através de GR, AF (determinísticos ou não) e ER, que:

1. Verifique se  duas  LR  L1 e L2 são iguais
2. Construa o AFD Mínimo M |
- T(M) seja o complemento de uma dada LR;
- T(M) = L1 ∩ L2;
- T(M) = L1 U L2;
- T(M) seja o reverso de uma dada LR.
3. Enumere as sentenças de tamanho n de uma dada LR.
4. Verifique se uma LR é vazia, finita ou infinita.

#### Observações
1. Representar os AF através de tabelas de transição (a tabela deve ser editável diretamente!).
2. Representar os estados (AF) e os não-terminais (GR) por letras maiúsculas.
(Identificar o estado inicial por  “->”  e os finais por  “*”).
3. Os símbolos do alfabeto devem ter tamanho 1 e podem ser limitados a dígitos e letras minúsculas.
4. Usar & para representar epsilon.
5. Os nomes de símbolos e estados (terminais e não-terminais) não devem ser pré-estabelecidos.
6. As GR devem seguir o padrão usado em aula (EX. S->aS|bB|c).
7. As ER devem seguir o padrão usado em aula  (EX. a*(b?c|d)*).
8. Os AF´s resultantes das operações devem ser visualizáveis na forma de tabela e utilizáveis diretamente em outras operações/verificações.
9. Além da corretude, serão avaliados aspectos de usabilidade e robustez da aplicação.
10. O trabalho deverá ser feito em duplas
11. A linguagem de programação é de livre escolha (porém deve ser dominada pelos 2 membros da equipe).
12. Caso sejam usados algoritmos diferentes dos usados em aula, eles devem ser documentados e exemplificados no relatório.
13. O trabalho deve ser encaminhado por e-mail, até 17/06, em um único arquivo zipado, contendo relatório(descrevendo a implementação), fonte (documentado), executável e testes. Usar como nome do arquivo o nome dos componentes da equipe (ex. JoaoF-MariaG.zip).
