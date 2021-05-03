grammar Dsl;

root       : SPACE? HASH? SPACE? EOL? issue child* SPACE? EOF;
issue      : '[' SPACE? project SPACE? ':' SPACE? type SPACE? ']' SPACE? summary EOL?;
child      : SPACE? dash+ SPACE? issue;

project     : WORD ;
type        : WORD ;
summary     : (WORD | SIGN | SPACE)+ ;
dash        : DASH SPACE* ;

HASH       : '#' ;
DASH       : '-' ;
WORD       : [a-zA-Z0-9'-]+ ;
SIGN       : [.,:-;(){}_*#^!~%&"?\\+=/] ;
SPACE      : (' ' | '\t')+ ;
EOL        : ('\n' | '\r' | '\n\r' | '\r\n')+ ;

WS : [\t\r\n]+ -> skip ; // skip spaces, tabs, newlines
ANY : . ;