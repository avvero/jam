grammar Dsl;

root       : SPACE? HASH? SPACE? EOL? issue issue_l1* SPACE? EOF;

issue_l1   : SPACE? dash SPACE? issue issue_l2*;
issue_l2   : SPACE? dash SPACE? dash SPACE? issue;

issue      : '[' SPACE? project SPACE? ':' SPACE? type SPACE? ']' SPACE? summary EOL?;

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