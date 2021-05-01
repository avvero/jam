grammar Dsl;

issue      : HASH? '[' project ':' type ']' summary;

project     : PROJECT ;
type        : TYPE ;
summary     : (WORD | sign)+ ;
sign        : SIGN;
space       : SPACE;

PROJECT    : [a-zA-Z0-9]+ ;
TYPE       : [a-zA-Z0-9-]+ ;
WORD       : [a-zA-Z0-9'-]+ ;
HASH       : '#' ;
SIGN       : [.,:-;(){}_*#^!~%&"?\\+=/] ;
SPACE      : (' ' | '\t')+ ;
NEWLINE    : ('\n' | '\r' | '\n\r' | '\r\n')+ ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
ANY : . ;