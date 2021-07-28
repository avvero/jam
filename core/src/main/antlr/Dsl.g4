grammar Dsl;

root       : HASH? issue child* EOF;
issue      : '[' key ':' type ']' summary (EOL | EOF);
child      : dash+ issue;

key        : WORD ;
type       : WORD SPACE* WORD*;
summary    : (WORD | SIGN | SPACE | ':') + ;
dash       : DASH SPACE* ;

HASH       : '#' ;
DASH       : '-' ;
WORD       : [a-zA-Z0-9'-]+ ;
SIGN       : [.,:-;(){}_*#^!~%&"?\\+=/]+ ;
SPACE      : (' ' | '\t')+ ;
EOL        : ('\n' | '\r' | '\n\r' | '\r\n')+ ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
ANY : . ;