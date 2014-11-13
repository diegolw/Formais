package manipulador.modelo;

public class ER {
	private String expressao;
	private No raiz;
	private int index;
	private int n_grupos;
	private Simone simone;
	
	public ER(String expr){
		this.expressao = expr;
		this.n_grupos = 1;
		this.index = 0;
		simone = new Simone(expressao);
	}
	
	public boolean temMais(){
		return index < expressao.length();
	}
	
	public boolean proximoIgualA(char ch){
		return temMais() && expressao.charAt(index) == ch;  //fazer
	}
	
	public char getNext(){
		if(index+1 < expressao.length())
			return expressao.charAt(index++);
		else
			return '$';
	}
	
	public char getChar(){ 
		return expressao.charAt(index);
	}
	
	public String regExpBasic(){
		int idx = index;
		boolean leftAnchored = false;
	    boolean rightAnchored = true;
	    
	    if (this.proximoIgualA('^')) {
	        leftAnchored = true;
	        this.getNext();
	      }
	    var node = this.regexp();
	    if (this.proximoIgualA('$')) {
	      rightAnchored = true;
	      this.getNext();
	    }
	    
	    
		
		return new No();
	}
	
	/*regexpBasic: function() {
    var index = this.index;
    var nextToken = this.peek();
    var node = null;
    var parenStartIndex, parenEndIndex;
    var groupNum;
    switch (nextToken) {
      case '[':
        this.get();
        node = this.charClass();
        if (!this.nextIs(']')) {
          // Parse error
          this.error = "Expcted ']', got '" + this.peek() + "'";
          this.index = index;
          return null;
        }
        this.get();
        break;*/
	
	public No charClass(){
		int idx = index;
		No node;
		boolean negado = false;
		if(proximoIgualA('!')){
			getNext();
			negado = true;
		}
		node = charRangesOrSingles();
		if(!node){
			index = idx;
			return node;
		}
		if(negado) {
			node = simone.negacao();
		}
		return node;
		
		
	}
	/*  charClass: function() {
    var index = this.index;
    var node = null;
    var negated = false;
    if (this.nextIs('^')) {
      this.get();
      negated = true;
    }
    node = this.charRangesOrSingles();
    if (!node) {
      this.index = index;
      return null;
    }
    if (negated) {
      node = new simone.Negacao(node);
    }
    return node;
  },*/
	
	public No charRangesOrSingles(){
	 int idx = index;
	 No node = charRange();
     if (!node) {
        node = singleEscapedChar();
      }
      if (!node) {
        node = operacao_char_unico();
      }
      if (!node) {
        this.index = index;
        return null;
      }
      No node2 = charRangesOrSingles();
      if (node2) {
        return new listachars.CharListNode(node, node2);
      }
      return node;
		
	}
/*charRangesOrSingles: function() {
    var index = this.index;
    var node = this.charRange();
    if (!node) {
      node = this.singleEscapedChar();
    }
    if (!node) {
      node = this.operacao_char_unico();
    }
    if (!node) {
      this.index = index;
      return null;
    }
    var node2 = this.charRangesOrSingles();
    if (node2) {
      return new listachars.CharListNode(node, node2);
    }
    return node;
  },*/
	
	public No charRange(){
		int idx = index;
		char char1 = operacao_char_unico();
		if (char1 == null) {
	      return null;
	    }
	    if (!this.nextIs('-')) {
	      this.index = idx;
	      return null;
	    }
	    this.getNext();
	    char char2 = this.operacao_char_unico();
	    if (char2 == null) {
	      this.index = idx;
	      return null;
	    }
	    return new listachars.ComprimentoCaractere(char1, char2);
		
	}
	
	/*charRange: function() {
    var index = this.index;
    var char1 = this.operacao_char_unico();
    if (!char1) {
      return null;
    }
    if (!this.nextIs('-')) {
      this.index = index;
      return null;
    }
    this.get();
    var char2 = this.operacao_char_unico();
    if (!char2) {
      this.index = index;
      return null;
    }
    return new listachars.ComprimentoCaractere(char1, char2);
  }*/
	
	public boolean operacao_char_unico(){
		char nextToken = getChar();
		String tokensReservados = "\\()[]|!$";		
	    return caractereUnico(this.getNext());
	}
	
	
	/*  operacao_char_unico: function() {
    var nextToken = this.peek();
    var disallowedTokens = "\\()[]|^$";
    if (disallowedTokens.indexOf(nextToken) != -1) {
      return null;
    } else {
      return new listachars.CaractereUnico(this.get());
    }
  },*/
	
	public boolean caractereUnico(char ch){
		if(ch == '.')
			return negarCaracteres();
		else
			return 
	}
	
	
/*
function CaractereUnico(ch) {
  this.ch = ch;
}

CaractereUnico.prototype = {
  capturar_caracteres: function() {
    if (this.ch == '.') {
      return negar_caracteres([{
        lb: '\n',
        ub: '\n'
      }]);
    }
    return [{
      lb: this.ch,
      ub: this.ch
    }];*/
	
	
	
	public String getExpressao() {
		return expressao;
	}

	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}

	public No getRaiz() {
		return raiz;
	}

	public void setRaiz(No raiz) {
		this.raiz = raiz;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getN_grupos() {
		return n_grupos;
	}

	public void setN_grupos(int n_grupos) {
		this.n_grupos = n_grupos;
	}
	
	

}
