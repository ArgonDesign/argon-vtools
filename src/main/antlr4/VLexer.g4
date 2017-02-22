lexer grammar VLexer;

channels {
  WHITESPACE,
  COMMENT,
  DIRECTIVE
}

// All IEEE 1364-2005 keywords
KWALWAYS              : 'always'              ;
KWAND                 : 'and'                 ;
KWASSIGN              : 'assign'              ;
KWAUTOMATIC           : 'automatic'           ;
KWBEGIN               : 'begin'               ;
KWBUF                 : 'buf'                 ;
KWBUFIF0              : 'bufif0'              ;
KWBUFIF1              : 'bufif1'              ;
KWCASE                : 'case'                ;
KWCASEX               : 'casex'               ;
KWCASEZ               : 'casez'               ;
KWCELL                : 'cell'                ;
KWCMOS                : 'cmos'                ;
KWCONFIG              : 'config'              ;
KWDEASSIGN            : 'deassign'            ;
KWDEFAULT             : 'default'             ;
KWDEFPARAM            : 'defparam'            ;
KWDESIGN              : 'design'              ;
KWDISABLE             : 'disable'             ;
KWEDGE                : 'edge'                ;
KWELSE                : 'else'                ;
KWEND                 : 'end'                 ;
KWENDCASE             : 'endcase'             ;
KWENDCONFIG           : 'endconfig'           ;
KWENDFUNCTION         : 'endfunction'         ;
KWENDGENERATE         : 'endgenerate'         ;
KWENDMODULE           : 'endmodule'           ;
KWENDPRIMITIVE        : 'endprimitive'        ;
KWENDSPECIFY          : 'endspecify'          ;
KWENDTABLE            : 'endtable'            ;
KWENDTASK             : 'endtask'             ;
KWEVENT               : 'event'               ;
KWFOR                 : 'for'                 ;
KWFORCE               : 'force'               ;
KWFOREVER             : 'forever'             ;
KWFORK                : 'fork'                ;
KWFUNCTION            : 'function'            ;
KWGENERATE            : 'generate'            ;
KWGENVAR              : 'genvar'              ;
KWHIGHZ0              : 'highz0'              ;
KWHIGHZ1              : 'highz1'              ;
KWIF                  : 'if'                  ;
KWIFNONE              : 'ifnone'              ;
KWINCDIR              : 'incdir'              ;
KWINCLUDE             : 'include'             ;
KWINITIAL             : 'initial'             ;
KWINOUT               : 'inout'               ;
KWINPUT               : 'input'               ;
KWINSTANCE            : 'instance'            ;
KWINTEGER             : 'integer'             ;
KWJOIN                : 'join'                ;
KWLARGE               : 'large'               ;
KWLIBLIST             : 'liblist'             ;
KWLIBRARY             : 'library'             ;
KWLOCALPARAM          : 'localparam'          ;
KWMACROMODULE         : 'macromodule'         ;
KWMEDIUM              : 'medium'              ;
KWMODULE              : 'module'              ;
KWNAND                : 'nand'                ;
KWNEGEDGE             : 'negedge'             ;
KWNMOS                : 'nmos'                ;
KWNOR                 : 'nor'                 ;
KWNOSHOWCANCELLED     : 'noshowcancelled'     ;
KWNOT                 : 'not'                 ;
KWNOTIF0              : 'notif0'              ;
KWNOTIF1              : 'notif1'              ;
KWOR                  : 'or'                  ;
KWOUTPUT              : 'output'              ;
KWPARAMETER           : 'parameter'           ;
KWPMOS                : 'pmos'                ;
KWPOSEDGE             : 'posedge'             ;
KWPRIMITIVE           : 'primitive'           ;
KWPULL0               : 'pull0'               ;
KWPULL1               : 'pull1'               ;
KWPULLDOWN            : 'pulldown'            ;
KWPULLUP              : 'pullup'              ;
KWPULSESTYLE_ONEVENT  : 'pulsestyle_onevent'  ;
KWPULSESTYLE_ONDETECT : 'pulsestyle_ondetect' ;
KWRCMOS               : 'rcmos'               ;
KWREAL                : 'real'                ;
KWREALTIME            : 'realtime'            ;
KWREG                 : 'reg'                 ;
KWRELEASE             : 'release'             ;
KWREPEAT              : 'repeat'              ;
KWRNMOS               : 'rnmos'               ;
KWRPMOS               : 'rpmos'               ;
KWRTRAN               : 'rtran'               ;
KWRTRANIF0            : 'rtranif0'            ;
KWRTRANIF1            : 'rtranif1'            ;
KWSCALARED            : 'scalared'            ;
KWSHOWCANCELLED       : 'showcancelled'       ;
KWSIGNED              : 'signed'              ;
KWSMALL               : 'small'               ;
KWSPECIFY             : 'specify'             ;
KWSPECPARAM           : 'specparam'           ;
KWSTRONG0             : 'strong0'             ;
KWSTRONG1             : 'strong1'             ;
KWSUPPLY0             : 'supply0'             ;
KWSUPPLY1             : 'supply1'             ;
KWTABLE               : 'table'               ;
KWTASK                : 'task'                ;
KWTIME                : 'time'                ;
KWTRAN                : 'tran'                ;
KWTRANIF0             : 'tranif0'             ;
KWTRANIF1             : 'tranif1'             ;
KWTRI                 : 'tri'                 ;
KWTRI0                : 'tri0'                ;
KWTRI1                : 'tri1'                ;
KWTRIAND              : 'triand'              ;
KWTRIOR               : 'trior'               ;
KWTRIREG              : 'trireg'              ;
KWUNSIGNED            : 'unsigned'            ;
KWUNSIGNED1           : 'unsigned1'           ;
KWUSE                 : 'use'                 ;
KWUWIRE               : 'uwire'               ;
KWVECTORED            : 'vectored'            ;
KWWAIT                : 'wait'                ;
KWWAND                : 'wand'                ;
KWWEAK0               : 'weak0'               ;
KWWEAK1               : 'weak1'               ;
KWWHILE               : 'while'               ;
KWWIRE                : 'wire'                ;
KWWOR                 : 'wor'                 ;
KWXNOR                : 'xnor'                ;
KWXOR                 : 'xor'                 ;
// Keywords not explicitly defined in the LRM
KWPATHPULSE           : 'PATHPULSE'           ;

// Compiler directive keywords
DKWBEGIN_KEYWORDS       : '`begin_keywords'       ;
DKWCELLDEFINE           : '`celldefine'           ;
DKWDEFAULT_NETTYPE      : '`default_nettype'      ;
DKWDEFINE               : '`define'               ;
DKWELSE                 : '`else'                 ;
DKWELSIF                : '`elsif'                ;
DKWEND_KEYWORDS         : '`end_keywords'         ;
DKWENDCELLDEFINE        : '`endcelldefine'        ;
DKWENDIF                : '`endif'                ;
DKWIFDEF                : '`ifdef'                ;
DKWIFNDEF               : '`ifndef'               ;
DKWINCLUDE              : '`include'              ;
DKWLINE                 : '`line'                 ;
DKWNOUNCONNECTED_DRIVE  : '`nounconnected_drive'  ;
DKWPRAGMA               : '`pragma'               ;
DKWRESETALL             : '`resetall'             ;
DKWTIMESCALE            : '`timescale'            ;
DKWUNCONNECTED_DRIVE    : '`unconnected_drive'    ;
DKWUNDEF                : '`undef'                ;

// Operators
BNOT:     '~'   ;
BAND:     '&'   ;
BNAND:    '~&'  ;
BOR:      '|'   ;
BNOR:     '~|'  ;
BXOR:     '^'   ;
BXNOR1:   '~^'  ;
BXNOR2:   '^~'  ;

ADD:      '+'   ;
SUB:      '-'   ;
MUL:      '*'   ;
DIV:      '/'   ;
MOD:      '%'   ;
POW:      '**'  ;

NOT:      '!'   ;
AND:      '&&'  ;
OR:       '||'  ;

LSR:      '>>'  ;
LSL:      '<<'  ;
ASR:      '>>>' ;
ASL:      '<<<' ;

EQ:       '=='  ;
NE:       '!='  ;
LT:       '<'   ;
LE:       '<='  ;
GT:       '>'   ;
GE:       '>='  ;

TEQ:      '===' ;
TNE:      '!==' ;

// Special forms not explicitly defined
AT:       '@'   ;
HASH:     '#'   ;
DOLLAR:   '$'   ;
EQUALITY: '='   ;
QUESTION: '?'   ;
LPAREN:   '('   ;
RPAREN:   ')'   ;
LBRACKET: '['   ;
RBRACKET: ']'   ;
LBRACE:   '{'   ;
RBRACE:   '}'   ;
DOT:      '.'   ;
COMA:     ','   ;
SEMI:     ';'   ;
COLON:    ':'   ;
PCOLON:   '+:'  ;
MCOLON:   '-:'  ;
SGT:      '->'  ;
EGT:      '=>'  ;
MGT:      '*>'  ;
TAND:     '&&&' ;

////////////////////////////////////////////////////////////////////////////////
// fragments
////////////////////////////////////////////////////////////////////////////////

fragment NL
  : '\r'? '\n'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.6 Numbers
////////////////////////////////////////////////////////////////////////////////

fragment DDIGIT
  : [0-9]
  ;

fragment XDIGIT
  : [xX]
  ;

fragment ZDIGIT
  : [zZ?]
  ;

fragment BDIGIT
  : [01]
  | XDIGIT
  | ZDIGIT
  ;

fragment ODIGIT
  : [0-7]
  | XDIGIT
  | ZDIGIT
  ;

fragment HDIGIT
  : [0-9a-fA-F]
  | XDIGIT
  | ZDIGIT
  ;

fragment DBASE
  : '\'' [sS]? [dD]
  ;

fragment BBASE
  : '\'' [sS]? [bB]
  ;

fragment OBASE
  : '\'' [sS]? [oO]
  ;

fragment HBASE
  : '\'' [sS]? [hH]
  ;

DVALUE
  : DDIGIT ('_' | DDIGIT)*
  ;

fragment BVALUE
  : BDIGIT ('_' | BDIGIT)*
  ;

fragment OVALUE
  : ODIGIT ('_' | ODIGIT)*
  ;

fragment HVALUE
  : HDIGIT ('_' | HDIGIT)*
  ;

fragment SIZE
  : [1-9] ('_' | DDIGIT)*
  ;

BINT
  : SIZE? BBASE BVALUE
  ;

OINT
  : SIZE? OBASE OVALUE
  ;

HINT
  : SIZE? HBASE HVALUE
  ;

DINT
  : SIZE? DBASE DVALUE
  ;

DX
  : SIZE? DBASE XDIGIT '_'*
  ;

DZ
  : SIZE? DBASE ZDIGIT '_'*
  ;

REAL
  : DVALUE '.' DVALUE
  ;

REALEXP
  : DVALUE ('.' DVALUE)? [eE] [+-]? DVALUE
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.8 Strings
////////////////////////////////////////////////////////////////////////////////

fragment STRINGESC
  : '\\"'
  | '\\\\'
  | '\\n'
  | '\\t'
  | '\\' ODIGIT ODIGIT ODIGIT
  ;

STRING
  : '"' (STRINGESC|.)*? '"'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.9.2 Comments
////////////////////////////////////////////////////////////////////////////////

ONE_LINE_COMMENT
  : '//' .*? NL -> channel(COMMENT)
  ;

BLOCK_COMMENT
  : '/*'  .*? '*/' -> channel(COMMENT)
  ;

////////////////////////////////////////////////////////////////////////////////
// A.9.3 Identifiers
////////////////////////////////////////////////////////////////////////////////

SYSID
  : '$'[a-zA-Z0-9_$][a-zA-Z0-9_$]*
  ;

fragment SIMPLEID
  : [a-zA-Z_][a-zA-Z0-9_$]*
  ;

fragment ESCAPEDID
  : '\\' [\u0021-\u007E]+
  ;

fragment MACROID
  : '`' SIMPLEID
  ;

IDENTIFIER
  : SIMPLEID
  | ESCAPEDID
  | MACROID
  ;

////////////////////////////////////////////////////////////////////////////////
// A.9.4 White space
////////////////////////////////////////////////////////////////////////////////

WS
  : ([ \t] | NL)+ -> channel(WHITESPACE)
  ;

////////////////////////////////////////////////////////////////////////////////
// Catch characters that failed to match any lexer rules
////////////////////////////////////////////////////////////////////////////////

ERRORCHAR : . ;