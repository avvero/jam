grammar Dsl;

root       : HASH? SPACE? EOL? issue child*;
issue      : '[' SPACE? project SPACE? ':' SPACE? type SPACE? ']' SPACE? summary end;
child      : DASH+  issue;

project     : WORD ;
type        : WORD ;
summary     : (WORD | SIGN | SPACE)+ ;
end         : EOF | EOL;

HASH       : '#' ;
DASH       : '-' ;
WORD       : [a-zA-Z0-9'-]+ ;
SIGN       : [.,:-;(){}_*#^!~%&"?\\+=/] ;
SPACE      : (' ' | '\t')+ ;
EOL        : ('\n' | '\r' | '\n\r' | '\r\n')+ ;

WS : [\t\r\n]+ -> skip ; // skip spaces, tabs, newlines
ANY : . ;