I – Definição:
Elaborar uma aplicação, com interface gráfica, para manipular GLC, envolvendo as seguintes 
verificações/operações:
1 – Ler, editar e salvar GLC;
2 – Fatorar, se possível, uma GLC usando até N passos (caso G não seja fatorável em N 
passos, considere-a não fatorável);
3 – Transformar G em uma GLC Própria;
4 – Eliminar recursões a esquerda de G;
5 - Verificar se L(G) é vazia, finita ou infinita.
II - Observações:
1 – Representar as GLC de forma textual (permitindo edição, leitura e gravação) seguindo o 
padrão dos exemplos abaixo:
a) E -> E + T | E - T | T
T -> T * F | T / F | F
F -> ( E ) | id
 
b) PROG -> DECL COM
DECL -> dec DECL | &
COM -> com COM | &
2 – deixar um espaço em branco entre os símbolos do lado direito.
3 – Representar não-terminais por letras maiúsculas (ou palavras envolvendo letras e dígitos 
começando com letra maiúscula).
4 – Representar terminais com um ou mais caracteres contíguos (quaisquer caracteres, exceto 
letras maiúsculas).
5 – Usar & para representar épsilon.
6 – O trabalho deverá ser feito em duplas.
7 – A linguagem de programação é de livre escolha (porém deve ser dominada pelos 2 
membros da equipe).
8 – Permitir a visualização das Gramáticas intermediárias bem como a reutilização de 
gramáticas resultantes de uma operação em outras operações.
9 – O trabalho deve ser encaminhado por e-mail, até 14/07, em um único arquivo zipado, 
contendo relatório (descrevendo a implementação), fonte (documentado), executável e testes. 
Usar como nome do arquivo o nome dos componentes da equipe (ex. JoaoF-MariaG.zip).
10 – Além da corretude, serão avaliados aspectos de usabilidade e robustez da aplicação.