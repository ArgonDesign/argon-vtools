parser grammar VParser;

options {
  tokenVocab = VLexer;
}

start
  : sourceText EOF
  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.1 Library source text
////////////////////////////////////////////////////////////////////////////////

//libraryText
//  : libraryDescription*
//  ;
//
//libraryDescription
//  : libraryDeclaration
//  | includeStatement
//  | configDeclaration
//  ;
//
//libraryDeclaration
//  : 'library' IDENTIFIER filePathSpec (',' filePathSpec)*
//     ('-' 'incdir' filePathSpec (',' filePathSpec)* )? ';'
//  ;
//
//includeStatement
//  : 'include' filePathSpec ';'
//  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.2 Verilog source text
////////////////////////////////////////////////////////////////////////////////

sourceText
  : (headerDirectives+=directive)*
    description*
    (footerDirectives+=directive)*
  ;

description
  : moduleDeclaration
//| udpDeclaration
  | configDeclaration
  ;

moduleDeclaration
  : attributeInstance*
    'module' IDENTIFIER moduleParameterPortList? listOfPorts ';'
      moduleItem*
    'endmodule'
  | attributeInstance*
    'module' IDENTIFIER moduleParameterPortList? listOfPortDeclarations? ';'
      nonPortModuleItem*
    'endmodule'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.3 Module parameters and ports
////////////////////////////////////////////////////////////////////////////////

moduleParameterPortList
  : '#' '(' parameterDeclaration (',' parameterDeclaration)* ')'
  ;

listOfPorts
  : '(' port (',' port)* ')'
  ;

listOfPortDeclarations
  : '(' portDeclaration (',' portDeclaration)* ')'
  | '(' ')'
  ;

port
  : portExpression?
  | '.' IDENTIFIER '(' portExpression? ')'
  ;

portExpression
  : portReference
  | '{' portReference (',' portReference)* '}'
  ;

portReference
  : IDENTIFIER ('[' constantRangeExpression ']')?
  ;

portDeclaration
  : attributeInstance* inoutDeclaration
  | attributeInstance* inputDeclaration
  | attributeInstance* outputDeclaration
  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.4 Module items
////////////////////////////////////////////////////////////////////////////////

moduleItem
  : portDeclaration ';'
  | nonPortModuleItem
  ;

moduleOrGenerateItem
  : attributeInstance* moduleOrGenerateItemDeclaration
  | attributeInstance* localParameterDeclaration ';'
  | attributeInstance* parameterOverride
  | attributeInstance* continuousAssign
  | attributeInstance* gateInstantiation
//| attributeInstance* udpInstantiation
  | attributeInstance* moduleInstantiation
  | attributeInstance* initialConstruct
  | attributeInstance* alwaysConstruct
  | attributeInstance* loopGenerateConstruct
  | attributeInstance* conditionalGenerateConstruct
  ;

moduleOrGenerateItemDeclaration
  : netDeclaration
  | regDeclaration
  | integerDeclaration
  | realDeclaration
  | timeDeclaration
  | realtimeDeclaration
  | eventDeclaration
  | genvarDeclaration
  | taskDeclaration
  | functionDeclaration
  ;

nonPortModuleItem
  : moduleOrGenerateItem
  | generateRegion
  | specifyBlock
  | attributeInstance* parameterDeclaration ';'
  | attributeInstance* specparamDeclaration
  ;

parameterOverride
  : 'defparam' listOfDefparamAssignments ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.5 Configuration source text
////////////////////////////////////////////////////////////////////////////////

configDeclaration
  : 'config' IDENTIFIER ';'
      designStatement
      configRuleStatement*
    'endconfig'
  ;

designStatement
  :  'design' ((IDENTIFIER '.')? IDENTIFIER)* ';'
  ;

configRuleStatement
  : 'default' liblistClause ';'
  | instClause liblistClause ';'
  | instClause useClause ';'
  | cellClause liblistClause ';'
  | cellClause useClause ';'
  ;

instClause
  : 'instance' instName
  ;

instName
  : IDENTIFIER ('.' IDENTIFIER)*
  ;

cellClause
  : 'cell' (IDENTIFIER '.')? IDENTIFIER
  ;

liblistClause
  : 'liblist' IDENTIFIER*
  ;

useClause
  : 'use' (IDENTIFIER '.')? IDENTIFIER (':' 'config')?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.1.1 Module parameter declarations
////////////////////////////////////////////////////////////////////////////////

localParameterDeclaration
  : 'localparam' 'signed'? vrange? listOfParamAssignments
  | 'localparam' parameterType listOfParamAssignments
  ;

parameterDeclaration
  : 'parameter' 'signed'? vrange? listOfParamAssignments
  | 'parameter' parameterType listOfParamAssignments
  ;

specparamDeclaration
  : 'specparam' vrange? listOfSpecparamAssignments ';'
  ;

parameterType
  : 'integer'
  | 'real'
  | 'realtime'
  | 'time'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.1.1 Port declarations
////////////////////////////////////////////////////////////////////////////////

inoutDeclaration
  : 'inout' netType? 'signed'? vrange? listOfIdentifiers
  ;

inputDeclaration
  : 'input' netType? 'signed'? vrange? listOfIdentifiers
  ;

outputDeclaration
  : 'output' netType? 'signed'? vrange? listOfIdentifiers
  | 'output' 'reg' 'signed'? vrange? listOfVariablePortIdentifiers
  | 'output' outputVariableType listOfVariablePortIdentifiers
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.1.3 Type declarations
////////////////////////////////////////////////////////////////////////////////

eventDeclaration
  : 'event' listOfEventIdentifiers ';'
  ;

integerDeclaration
  : 'integer' listOfVariableIdentifiers ';'
  ;

netDeclaration
  : netType
    'signed'? delay3? listOfNetIdentifiers ';'
  | netType driveStrength?
    'signed'? delay3? listOfNetDeclAssignments ';'
  | netType ('vectored' | 'scalared')?
    'signed'? vrange delay3? listOfNetIdentifiers ';'
  | netType driveStrength? ('vectored' | 'scalared')?
    'signed'? vrange delay3? listOfNetDeclAssignments ';'
  | 'trireg' chargeStrength?
    'signed'? delay3? listOfNetIdentifiers ';'
  | 'trireg' driveStrength?
    'signed'? delay3? listOfNetDeclAssignments ';'
  | 'trireg' chargeStrength? ('vectored' | 'scalared')?
    'signed'? vrange delay3? listOfNetIdentifiers ';'
  | 'trireg' driveStrength? ('vectored' | 'scalared')?
    'signed' vrange delay3? listOfNetDeclAssignments ';'
  ;

realDeclaration
  : 'real' listOfRealIdentifiers ';'
  ;

realtimeDeclaration
  : 'realtime' listOfRealIdentifiers ';'
  ;

regDeclaration
  : 'reg' 'signed'? vrange? listOfVariableIdentifiers ';'
  ;

timeDeclaration
  : 'time' listOfVariableIdentifiers ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.2.1 Net and variable types
////////////////////////////////////////////////////////////////////////////////

netType
  : 'wire'
  | 'supply0'
  | 'supply1'
  | 'tri'
  | 'triand'
  | 'trior'
  | 'tri0'
  | 'tri1'
  | 'uwire'
  | 'wand'
  | 'wor'
  ;

outputVariableType
  : 'integer'
  | 'time'
  ;

realType
  : IDENTIFIER dimension*
  | IDENTIFIER '=' constantExpression
  ;

variableType
  : IDENTIFIER dimension*
  | IDENTIFIER '=' constantExpression
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.2.2 Strengths
////////////////////////////////////////////////////////////////////////////////

driveStrength
  : '(' strength0 ',' strength1 ')'
  | '(' strength1 ',' strength0 ')'
  | '(' strength0 ',' 'highz1' ')'
  | '(' strength1 ',' 'highz0' ')'
  | '(' 'highz0' ',' strength1 ')'
  | '(' 'highz1' ',' strength0 ')'
  ;

strength0
  : 'supply0'
  | 'strong0'
  | 'pull0'
  | 'weak0'
  ;

strength1
  : 'supply1'
  | 'strong1'
  | 'pull1'
  | 'weak1'
  ;

chargeStrength
  : '(' 'small' ')'
  | '(' 'medium' ')'
  | '(' 'large' ')'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.2.3 Delays
////////////////////////////////////////////////////////////////////////////////

delay3
  : '#' delayValue
  | '#' '(' mintypmaxExpression (',' mintypmaxExpression (',' mintypmaxExpression)?)? ')'
  ;

delay2
  : '#' delayValue
  | '#' '(' mintypmaxExpression (',' mintypmaxExpression)? ')'
  ;

delayValue
  : DVALUE
//| realNumber
  | IDENTIFIER
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.3 Declaration lists
////////////////////////////////////////////////////////////////////////////////

listOfDefparamAssignments
  : defparamAssignment (',' defparamAssignment)*
  ;

listOfEventIdentifiers
  : IDENTIFIER dimension* (',' IDENTIFIER dimension*)*
  ;

listOfNetDeclAssignments
  : netDeclAssignment (',' netDeclAssignment )*
  ;

listOfNetIdentifiers
  : IDENTIFIER dimension* (',' IDENTIFIER dimension*)*
  ;

listOfParamAssignments
  : paramAssignment (',' paramAssignment)*
  ;

listOfRealIdentifiers
  : realType (',' realType)*
  ;

listOfSpecparamAssignments
  : specparamAssignment (',' specparamAssignment)*
  ;

listOfVariableIdentifiers
  : variableType (',' variableType)*
  ;

listOfVariablePortIdentifiers
  : IDENTIFIER ('=' constantExpression)? (',' IDENTIFIER ('=' constantExpression)?)*
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.4 Declaration assignmets
////////////////////////////////////////////////////////////////////////////////

defparamAssignment
  : hierarchicalIdentifier '=' constantMintypmaxExpression
  ;

netDeclAssignment
  : IDENTIFIER '=' expression
  ;

paramAssignment
  : IDENTIFIER '=' constantMintypmaxExpression
  ;

specparamAssignment
  : IDENTIFIER '=' constantMintypmaxExpression
  | pulseControlSpecparam
  ;

pulseControlSpecparam
  : 'PATHPULSE' '$' '=' '(' constantMintypmaxExpression (',' constantMintypmaxExpression)? ')'
  | 'PATHPULSE' '$' specifyTerminalDescriptor '$'
    specifyTerminalDescriptor '=' '(' constantMintypmaxExpression (',' constantMintypmaxExpression)? ')'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.5 Declaration ranges
////////////////////////////////////////////////////////////////////////////////

dimension
  : '[' constantExpression ':' constantExpression ']'
  ;

vrange
  : '[' constantExpression ':' constantExpression ']'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.6 Function declarations
////////////////////////////////////////////////////////////////////////////////

functionDeclaration
  : 'function' 'automatic'? functionRangeOrType? IDENTIFIER ';'
      functionItemDeclaration+
      statement
    'endfunction'
  | 'function' 'automatic'? functionRangeOrType? IDENTIFIER '(' functionPortList ')' ';'
      blockItemDeclaration*
      statement
    'endfunction'
  ;

functionItemDeclaration
  : blockItemDeclaration
  | attributeInstance* tfInputDeclaration ';'
  ;

functionPortList
  : attributeInstance* tfInputDeclaration (',' attributeInstance* tfInputDeclaration)*
  ;

functionRangeOrType
  : 'signed'
  | vrange
  | 'signed' vrange
  | 'integer'
  | 'real'
  | 'realtime'
  | 'time'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.7 Task declarations
////////////////////////////////////////////////////////////////////////////////

taskDeclaration
  : 'task' 'automatic'? IDENTIFIER ';'
      taskItemDeclaration*
      statementOrNull
    'endtask'
  | 'task' 'automatic'? IDENTIFIER '(' taskPortList? ')' ';'
      blockItemDeclaration*
      statementOrNull
    'endtask'
  ;
taskItemDeclaration
  : blockItemDeclaration
  | attributeInstance* tfInputDeclaration ';'
  | attributeInstance* tfOutputDeclaration ';'
  | attributeInstance* tfInoutDeclaration ';'
  ;

taskPortList
  : taskPortItem (',' taskPortItem)*
  ;

taskPortItem
  : attributeInstance* tfInputDeclaration
  | attributeInstance* tfOutputDeclaration
  ;

tfInputDeclaration
  : 'input' 'reg'? 'signed'? vrange? listOfIdentifiers
  | 'input' taskPortType listOfIdentifiers
  ;

tfOutputDeclaration
  : 'output' 'reg'? 'signed'? vrange? listOfIdentifiers
  | 'output' taskPortType listOfIdentifiers
  ;

tfInoutDeclaration
  : 'inout' 'reg'? 'signed'? vrange? listOfIdentifiers
  | 'inout' taskPortType listOfIdentifiers
  ;

taskPortType
  : 'integer'
  | 'real'
  | 'realtime'
  | 'time'
  | attributeInstance* tfInoutDeclaration
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.8 Block item declarations
////////////////////////////////////////////////////////////////////////////////

blockItemDeclaration
  : attributeInstance* 'reg' 'signed'? vrange? listOfBlockVariableIdentifiers ';'
  | attributeInstance* 'integer' listOfBlockVariableIdentifiers ';'
  | attributeInstance* 'time' listOfBlockVariableIdentifiers ';'
  | attributeInstance* 'real' listOfBlockRealIdentifiers ';'
  | attributeInstance* 'realtime' listOfBlockRealIdentifiers ';'
  | attributeInstance* eventDeclaration
  | attributeInstance* localParameterDeclaration ';'
  | attributeInstance* parameterDeclaration ';'
  ;

listOfBlockVariableIdentifiers
  : blockVariableType (',' blockVariableType)*
  ;

listOfBlockRealIdentifiers
  : blockRealType (',' blockRealType)*
  ;

blockVariableType
  : IDENTIFIER dimension*
  ;

blockRealType
  : IDENTIFIER dimension*
  ;

////////////////////////////////////////////////////////////////////////////////
// A.3.1 Primitive instantiation and instances
////////////////////////////////////////////////////////////////////////////////

gateInstantiation
  : cmosSwitchtype delay3? cmosSwitchInstance (',' cmosSwitchInstance)* ';'
  | enableGatetype driveStrength? delay3? enableGateInstance (',' enableGateInstance)* ';'
  | mosSwitchtype delay3? mosSwitchInstance (',' mosSwitchInstance)* ';'
  | nInputGatetype driveStrength? delay2? nInputGateInstance (',' nInputGateInstance)* ';'
  | nOutputGatetype driveStrength? delay2? nOutputGateInstance (',' nOutputGateInstance)* ';'
  | passEnSwitchtype delay2? passEnableSwitchInstance (',' passEnableSwitchInstance)* ';'
  | passSwitchtype passSwitchInstance (',' passSwitchInstance)* ';'
  | 'pulldown' pulldownStrength? pullGateInstance (',' pullGateInstance)* ';'
  | 'pullup' pullupStrength? pullGateInstance (',' pullGateInstance)* ';'
  ;

cmosSwitchInstance
  : nameOfGateInstance?
    '(' netLvalue ',' expression ',' expression ',' expression ')'
  ;

enableGateInstance
  : nameOfGateInstance?
    '(' netLvalue ',' expression ',' expression ')'
  ;

mosSwitchInstance
  : nameOfGateInstance?
    '(' netLvalue ',' expression ',' expression ')'
  ;

nInputGateInstance
  : nameOfGateInstance?
    '(' netLvalue ',' expression (',' expression)* ')'
  ;

nOutputGateInstance
  : nameOfGateInstance?
    '(' netLvalue (',' netLvalue)* ',' expression ')'
  ;

passSwitchInstance
  : nameOfGateInstance?
    '(' netLvalue ',' netLvalue ')' ';'
  ;

passEnableSwitchInstance
  : nameOfGateInstance?
    '(' netLvalue ',' netLvalue ',' expression ')'
  ;

pullGateInstance
  : nameOfGateInstance?
    '(' netLvalue ')'
  ;

nameOfGateInstance
  : IDENTIFIER vrange?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.3.2 Primitive strengths
////////////////////////////////////////////////////////////////////////////////

pulldownStrength
  : '(' strength0 ',' strength1 ')'
  | '(' strength1 ',' strength0 ')'
  | '(' strength0 ')'
  ;

pullupStrength
  : '(' strength0 ',' strength1 ')'
  | '(' strength1 ',' strength0 ')'
  | '(' strength1 ')'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.3.3 Primitive terminals
////////////////////////////////////////////////////////////////////////////////

// Eliminated

////////////////////////////////////////////////////////////////////////////////
// A.3.4 Primitive gate and switch types
////////////////////////////////////////////////////////////////////////////////

cmosSwitchtype
  : 'cmos'
  | 'rcmos'
  ;

enableGatetype
  : 'bufif0'
  | 'bufif1'
  | 'notif0'
  | 'notif1'
  ;

mosSwitchtype
  : 'nmos'
  | 'pmos'
  | 'rnmos'
  | 'rpmos'
  ;

nInputGatetype
  : 'and'
  | 'nand'
  | 'or'
  | 'nor'
  | 'xor'
  | 'xnor'
  ;

nOutputGatetype
  : 'buf'
  | 'not'
  ;

passEnSwitchtype
  : 'tranif0'
  | 'tranif1'
  | 'rtranif1'
  | 'rtranif0'
  ;

passSwitchtype
  : 'tran'
  | 'rtran'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.4.1 Module instantiation
////////////////////////////////////////////////////////////////////////////////

moduleInstantiation
  : IDENTIFIER parameterValueAssignment? moduleInstance (',' moduleInstance)* ';'
  ;

parameterValueAssignment
  : '#' '(' listOfParameterAssignments ')'
  ;

listOfParameterAssignments
  : orderedParameterAssignment (',' orderedParameterAssignment)*
  | namedParameterAssignment (',' namedParameterAssignment)*
  ;

orderedParameterAssignment
  : expression
  ;

namedParameterAssignment
  : '.' IDENTIFIER '(' mintypmaxExpression? ')'
  ;

moduleInstance
  : nameOfModuleInstance '(' listOfPortConnections ')'
  ;

nameOfModuleInstance
  : IDENTIFIER vrange?
  ;

listOfPortConnections
  : orderedPortConnection? (',' orderedPortConnection?)*
  | namedPortConnection (',' namedPortConnection)*
  ;

orderedPortConnection
  : attributeInstance* expression
  | attributeInstance+
  ;

namedPortConnection
  : attributeInstance* '.' IDENTIFIER '(' expression? ')'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.4.2 Generate construct
////////////////////////////////////////////////////////////////////////////////

generateRegion
  : 'generate' moduleOrGenerateItem* 'endgenerate'
  ;

genvarDeclaration
  : 'genvar' listOfIdentifiers ';'
  ;

loopGenerateConstruct
  : 'for' '(' genvarInitialization ';' genvarExpression ';' genvarIteration ')'
    generateBlock
  ;

genvarInitialization
  : IDENTIFIER '=' constantExpression
  ;

genvarExpression
  : genvarPrimary
  | unaryOperator attributeInstance* genvarPrimary
  | genvarExpression binaryOperator attributeInstance* genvarExpression
  | genvarExpression '?' attributeInstance* genvarExpression ':' genvarExpression
  ;

genvarIteration
  : IDENTIFIER '=' genvarExpression
  ;

genvarPrimary
  : constantPrimary
  | IDENTIFIER
  ;

conditionalGenerateConstruct
  : ifGenerateConstruct
  | caseGenerateConstruct
  ;

ifGenerateConstruct
  : 'if' '(' constantExpression ')' ifBlock=generateBlockOrNull
    ('else' 'if' '(' constantExpression ')' elseIfBlocks+=generateBlockOrNull)*
    ('else' elseBlock+=generateBlockOrNull)?
  ;

caseGenerateConstruct
  : 'case' '(' constantExpression ')'
      caseGenerateItem+
    'endcase'
  ;

caseGenerateItem
  : constantExpression (',' constantExpression)* ':' generateBlockOrNull
  | 'default' ':'? generateBlockOrNull
  ;

generateBlock
  : moduleOrGenerateItem                                    # generateBlockWithoutBeginEnd
  | 'begin' (':' IDENTIFIER)?  moduleOrGenerateItem* 'end'  # generateBlockWithBeginEnd
  ;

generateBlockOrNull
  : generateBlock
  | ';'
  ;

//////////////////////////////////////////////////////////////////////////////////
//// A.5.1 UDP declaration
//////////////////////////////////////////////////////////////////////////////////
//
//udpDeclaration
//  : attributeInstance*
//    'primitive' IDENTIFIER '(' udpPortList ')' ';'
//      udpPortDeclaration+
//      udpBody
//    'endprimitive'
//  | attributeInstance*
//    'primitive' IDENTIFIER '(' udpDeclarationPortList ')' ';'
//      udpBody
//    'endprimitive '
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.5.2 UDP ports
//////////////////////////////////////////////////////////////////////////////////
//
//udpPortList
//  : IDENTIFIER ',' IDENTIFIER (',' IDENTIFIER)*
//  ;
//
//udpDeclarationPortList
//  : udpOutputDeclaration ',' udpInputDeclaration (',' udpInputDeclaration)*
//  ;
//
//udpPortDeclaration
//  : udpOutputDeclaration ';'
//  | udpInputDeclaration ';'
//  | udpRegDeclaration ';'
//  ;
//
//udpOutputDeclaration
//  : attributeInstance* output IDENTIFIER
//  | attributeInstance* output reg IDENTIFIER ('=' constantExpression)?
//  ;
//
//udpInputDeclaration
//  : attributeInstance* input listOfIdentifiers
//  ;
//
//udpRegDeclaration
//  : attributeInstance* reg IDENTIFIER
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.5.3 UDP body
//////////////////////////////////////////////////////////////////////////////////
//
//udpBody
//  : combinationalBody
//  | sequentialBody
//  ;
//
//combinationalBody
//  : 'table' combinationalEntry+ 'endtable'
//  ;
//
//combinationalEntry
//  : levelInputList ':' outputSymbol ';'
//  ;
//
//sequentialBody
//  : udpInitialStatement? 'table' sequentialEntry+ 'endtable'
//  ;
//
//udpInitialStatement
//  : 'initial' IDENTIFIER '=' initVal ';'
//  ;
//
//initVal
//  : '1\'b0'
//  | '1\'b1'
//  | '1\'bx'
//  | '1\'bX'
//  | '1\'B0'
//  | '1\'B1'
//  | '1\'Bx'
//  | '1\'BX'
//  | '1'
//  | '0'
//  ;
//
//sequentialEntry
//  : seqInputList ':' levelSymbol ':' nextState ';'
//  ;
//
//seqInputList
//  : levelInputList
//  | edgeInputList
//  ;
//
//levelInputList
//  : levelSymbol+
//  ;
//
//edgeInputList
//  : levelSymbol* edgeIndicator levelSymbol*
//  ;
//
//edgeIndicator
//  : '(' levelSymbol levelSymbol ')'
//  | edgeSymbol
//  ;
//
//nextState
//  : outputSymbol
//  | '-'
//  ;
//
//outputSymbol
//  : '0'
//  | '1'
//  | 'x'
//  | 'X'
//  ;
//
//levelSymbol
//  : '0'
//  | '1'
//  | 'x'
//  | 'X'
//  | '?'
//  | 'b'
//  | 'B'
//  ;
//
//edgeSymbol
//  : 'r'
//  | 'R'
//  | 'f'
//  | 'F'
//  | 'p'
//  | 'P'
//  | 'n'
//  | 'N'
//  | '*'
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.5.4 UDP instantiation
//////////////////////////////////////////////////////////////////////////////////
//
//udpInstantiation
//  : IDENTIFIER driveStrength? delay2? udpInstance (',' udpInstance)* ';'
//  ;
//
//udpInstance
//  : nameOfUdpInstance? '(' netLvalue ',' expression (',' expression)* ')'
//  ;
//
//nameOfUdpInstance
//  : IDENTIFIER vrange?
//  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.1 Continuous assignment startements
////////////////////////////////////////////////////////////////////////////////

continuousAssign
  : 'assign' driveStrength? delay3? listOfNetAssignments ';'
  ;

listOfNetAssignments
  : netAssignment (',' netAssignment)*
  ;

netAssignment
  : netLvalue '=' expression
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.2 Procedural blocks and assignments
////////////////////////////////////////////////////////////////////////////////

initialConstruct
  : 'initial' statement
  ;

alwaysConstruct
  : 'always' attributeInstance* '@' '(' eventExpression ')' statement   # alwaysEvent
  | 'always' attributeInstance* atStar                      statement   # alwaysAtStar
  | 'always' attributeInstance* '#' delayValue              statement   # alwaysDelay
  ;

atStar
  : '@' '*'
  | '@' '(' '*' ')'
  ;

blockingAssignment
  : variableLvalue '=' delayOrEventControl? expression
  ;

nonblockingAssignment
  : variableLvalue '<=' delayOrEventControl? expression
  ;

proceduralContinuousAssignments
  : 'assign' variableAssignment
  | 'deassign' variableLvalue
  | 'force' variableAssignment
  | 'force' netAssignment
  | 'release' variableLvalue
  | 'release' netLvalue
  ;

variableAssignment
  : variableLvalue '=' expression
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.3 Parallel and sequential blocks
////////////////////////////////////////////////////////////////////////////////

parBlock
  : 'fork' (':' IDENTIFIER blockItemDeclaration* )?
      statement*
    'join'
  ;

seqBlock
  : 'begin' (':' IDENTIFIER  blockItemDeclaration*)?
      statement*
    'end'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.4 Statements
////////////////////////////////////////////////////////////////////////////////

statement
  : attributeInstance* blockingAssignment ';'
  | attributeInstance* caseStatement
  | attributeInstance* conditionalStatement
  | attributeInstance* disableStatement
  | attributeInstance* eventTrigger
  | attributeInstance* loopStatement
  | attributeInstance* nonblockingAssignment ';'
  | attributeInstance* parBlock
  | attributeInstance* proceduralContinuousAssignments ';'
  | attributeInstance* proceduralTimingControlStatement
  | attributeInstance* seqBlock
  | attributeInstance* systemTaskEnable
  | attributeInstance* taskEnable
  | attributeInstance* waitStatement
  ;

statementOrNull
  : statement
  | attributeInstance* ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.5 Timing control statements
////////////////////////////////////////////////////////////////////////////////

delayControl
  : '#' delayValue
  | '#' '(' mintypmaxExpression ')'
  ;

delayOrEventControl
  : delayControl
  | eventControl
  | 'repeat' '(' expression ')' eventControl
  ;

disableStatement
  : 'disable' hierarchicalIdentifier ';'
  ;

eventControl
  : '@' hierarchicalIdentifier
  | '@' '(' eventExpression ')'
  | '@' '*'
  | '@' '(' '*' ')'
  ;

eventTrigger
  : '->' hierarchicalIdentifier ('[' expression ']')* ';'
  ;

eventExpression
  : expression
  | 'posedge' expression
  | 'negedge' expression
  | eventExpression 'or' eventExpression
  | eventExpression ',' eventExpression
  ;

proceduralTimingControl
  : delayControl
  | eventControl
  ;

proceduralTimingControlStatement
  : proceduralTimingControl statementOrNull
  ;

waitStatement
  : 'wait' '(' expression ')' statementOrNull
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.6 Conditional statements
////////////////////////////////////////////////////////////////////////////////

conditionalStatement
  : 'if' '(' expression ')' statementOrNull ('else' statementOrNull)?
  | ifElseIfStatement
  ;

ifElseIfStatement
  : 'if' '(' expression ')' statementOrNull
    ('else' 'if' '(' expression ')' statementOrNull )*
    ('else' statementOrNull)?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.7 Case statements
////////////////////////////////////////////////////////////////////////////////

caseStatement
  : 'case' '(' expression ')'
      caseItem+
    'endcase'
  | 'casez' '(' expression ')'
      caseItem+
    'endcase'
  | 'casex' '(' expression ')'
      caseItem+
    'endcase'
  ;

caseItem
  : expression (',' expression)* ':' statementOrNull
  | 'default' ':'? statementOrNull
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.8 looping statements
////////////////////////////////////////////////////////////////////////////////

loopStatement
  : 'forever' statement
  | 'repeat' '(' expression ')' statement
  | 'while' '(' expression ')' statement
  | 'for' '(' variableAssignment ';' expression ';' variableAssignment ')' statement
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.9 Task enable statemetns
////////////////////////////////////////////////////////////////////////////////

systemTaskEnable
  : SYSID ('(' expression? (',' expression?)* ')')? ';'
  ;

taskEnable
  : hierarchicalIdentifier ('(' expression (',' expression)* ')')? ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.7.1 Specify block declaration
////////////////////////////////////////////////////////////////////////////////

specifyBlock
  : 'specify' specifyItem* 'endspecify'
  ;

specifyItem
  : specparamDeclaration
  | pulsestyleDeclaration
  | showcancelledDeclaration
  | pathDeclaration
//| systemTimingCheck
  ;

pulsestyleDeclaration
  : 'pulsestyle_onevent' listOfPaths ';'
  | 'pulsestyle_ondetect' listOfPaths ';'
  ;

showcancelledDeclaration
  : 'showcancelled' listOfPaths ';'
  | 'noshowcancelled' listOfPaths ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.7.2 Specify path declarations
////////////////////////////////////////////////////////////////////////////////

pathDeclaration
  : simplePathDeclaration ';'
  | edgeSensitivePathDeclaration ';'
  | stateDependentPathDeclaration ';'
  ;

simplePathDeclaration
  : parallelPathDescription '=' pathDelayValue
  | fullPathDescription '=' pathDelayValue
  ;

parallelPathDescription
  : '(' specifyTerminalDescriptor polarityOperator? '=>' specifyTerminalDescriptor ')'
  ;

fullPathDescription
  : '(' listOfPaths polarityOperator? '*>' listOfPaths ')'
  ;

listOfPaths
  : specifyTerminalDescriptor (',' specifyTerminalDescriptor)*
  ;

////////////////////////////////////////////////////////////////////////////////
// A.7.3 Specify block terminals
////////////////////////////////////////////////////////////////////////////////

specifyTerminalDescriptor
  : IDENTIFIER ('[' constantRangeExpression ']')?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.7.4 Specify path delays
////////////////////////////////////////////////////////////////////////////////

pathDelayValue
  : listOfPathDelayExpressions
  | '(' listOfPathDelayExpressions ')'
  ;

listOfPathDelayExpressions
  : constantMintypmaxExpression (',' constantMintypmaxExpression)*
  ;

edgeSensitivePathDeclaration
  : parallelEdgeSensitivePathDescription '=' pathDelayValue
  | fullEdgeSensitivePathDescription '=' pathDelayValue
  ;

parallelEdgeSensitivePathDescription
  : '(' edgeIdentifier? specifyTerminalDescriptor '=>'
    '(' specifyTerminalDescriptor polarityOperator? ':' expression ')' ')'
  ;

fullEdgeSensitivePathDescription
  : '(' edgeIdentifier? listOfPaths '*>'
    '(' listOfPaths polarityOperator? ':' expression ')' ')'
  ;

edgeIdentifier
  : 'posedge'
  | 'negedge'
  ;

stateDependentPathDeclaration
  : 'if' '(' modulePathExpression ')' simplePathDeclaration
  | 'if' '(' modulePathExpression ')' edgeSensitivePathDeclaration
  | 'ifnone' simplePathDeclaration
  ;

polarityOperator
  : '+'
  | '-'
  ;

//////////////////////////////////////////////////////////////////////////////////
//// A.7.5.1 System timing check comands
//////////////////////////////////////////////////////////////////////////////////
//
//systemTimingCheck
//  : $setupTimingCheck
//  | $holdTimingCheck
//  | $setupholdTimingCheck
//  | $recoveryTimingCheck
//  | $removalTimingCheck
//  | $recremTimingCheck
//  | $skewTimingCheck
//  | $timeskewTimingCheck
//  | $fullskewTimingCheck
//  | $periodTimingCheck
//  | $widthTimingCheck
//  | $nochangeTimingCheck
//  ;
//
//$setupTimingCheck
//  : '$setup' '(' timingCheckEvent ',' timingCheckEvent ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$holdTimingCheck
//  : '$hold' '(' timingCheckEvent ',' timingCheckEvent ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$setupholdTimingCheck
//  : '$setuphold' '(' timingCheckEvent ',' timingCheckEvent ',' expression ',' expression
//    (',' IDENTIFIER? (',' mintypmaxExpression? (',' mintypmaxExpression? (',' delayedReference? (',' delayedData?)?)?)?)?)? ')' ';'
//  ;
//
//$recoveryTimingCheck
//  : '$recovery' '(' timingCheckEvent ',' timingCheckEvent ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$removalTimingCheck
//  : '$removal' '(' timingCheckEvent ',' timingCheckEvent ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$recremTimingCheck
//  : '$recrem' '(' timingCheckEvent ',' timingCheckEvent ',' expression ',' expression
//    (',' IDENTIFIER? (',' mintypmaxExpression? (',' mintypmaxExpression? (',' delayedReference? (',' delayedData?)?)?)?)?)? ')' ';'
//  ;
//
//$skewTimingCheck
//  : '$skew' '(' timingCheckEvent ',' timingCheckEvent ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$timeskewTimingCheck
//  : '$timeskew' '(' timingCheckEvent ',' timingCheckEvent ',' expression
//    (',' IDENTIFIER? (',' constantExpression? (',' constantExpression?)?)?)? ')' ';'
//  ;
//
//$fullskewTimingCheck
//  : '$fullskew' '(' timingCheckEvent ',' timingCheckEvent ',' expression ',' expression
//    (',' IDENTIFIER? (',' constantExpression? (',' constantExpression?)?)?)? ')' ';'
//  ;
//
//$periodTimingCheck
//  : '$period' '(' controlledTimingCheckEvent ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$widthTimingCheck
//  : '$width' '(' controlledTimingCheckEvent ',' expression
//    (',' constantExpression (',' IDENTIFIER)?)? ')' ';'
//  ;
//
//$nochangeTimingCheck
//  : '$nochange' '(' timingCheckEvent ',' timingCheckEvent ',' mintypmaxExpression ','
//    mintypmaxExpression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.7.5.2 system timing check command arguments
//////////////////////////////////////////////////////////////////////////////////
//
//delayedData
//  : IDENTIFIER constantMintypmaxExpression?
//  ;
//
//delayedReference
//  : IDENTIFIER constantMintypmaxExpression?
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.7.5.3 System timing check event definitions
//////////////////////////////////////////////////////////////////////////////////
//
//timingCheckEvent
//  : timingCheckEventControl? specifyTerminalDescriptor ('&&&' timingCheckCondition)?
//  ;
//
//controlledTimingCheckEvent
//  : timingCheckEventControl specifyTerminalDescriptor ('&&&' timingCheckCondition)?
//  ;
//
//timingCheckEventControl
//  : 'posedge'
//  | 'negedge'
//  | edgeControlSpecifier
//  ;
//
//edgeControlSpecifier
//  : 'edge' (edgeDescriptor (',' edgeDescriptor)*)?
//  ;
//
//edgeDescriptor2
//  : '01'
//  | '10'
//  | zOrX zeroOrOne
//  | zeroOrOne zOrX
//  ;
//
//zeroOrOne
//  : '0'
//  | '1'
//  ;
//
//zOrX
//  : 'x'
//  | 'X'
//  | 'z'
//  | 'Z'
//  ;
//
//timingCheckCondition
//  : scalarTimingCheckCondition
//  | '(' scalarTimingCheckCondition ')'
//  ;
//
//scalarTimingCheckCondition
//  : expression
//  | '~' expression
//  | expression '==' scalarConstant
//  | expression '===' scalarConstant
//  | expression '!=' scalarConstant
//  | expression '!==' scalarConstant
//  ;
//
//scalarConstant
//  : '1\'b0'
//  | '1\'b1'
//  | '1\'B0'
//  | '1\'B1'
//  | '\'b0'
//  | '\'b1'
//  | '\'B0'
//  | '\'B1'
//  | '1'
//  | '0'
//  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.1 Concatenations
////////////////////////////////////////////////////////////////////////////////

concatenation
  : '{' expression (',' expression)* '}'
  ;

constantConcatenation
  : '{' constantExpression (',' constantExpression)* '}'
  ;

constantMultipleConcatenation
  : '{' constantExpression constantConcatenation '}'
  ;

modulePathConcatenation
  : '{' modulePathExpression (',' modulePathExpression)* '}'
  ;

modulePathMultipleConcatenation
  : '{' constantExpression modulePathConcatenation '}'
  ;

multipleConcatenation
  : '{' constantExpression concatenation '}'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.2 Function calls
////////////////////////////////////////////////////////////////////////////////

constantFunctionCall
  : IDENTIFIER attributeInstance* '(' constantExpression (',' constantExpression)* ')'
  ;

constantSystemFunctionCall
  : SYSID '(' constantExpression (',' constantExpression)* ')'
  ;

functionCall
  : hierarchicalIdentifier attributeInstance* '(' expression (',' expression)* ')'
  ;

systemFunctionCall
  : SYSID ('(' expression (',' expression)* ')')?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.3 Expressions
////////////////////////////////////////////////////////////////////////////////

constantExpression
  : constantPrimary
  | unaryOperator attributeInstance* constantPrimary
  | constantExpression binaryOperator attributeInstance* constantExpression
  | constantExpression '?' attributeInstance* constantExpression ':' constantExpression
  ;

constantMintypmaxExpression
  : constantExpression
  | constantExpression ':' constantExpression ':' constantExpression
  ;

constantRangeExpression
  : constantExpression
  | constantExpression ':' constantExpression
  | constantExpression '+:' constantExpression
  | constantExpression '-:' constantExpression
  ;

expression
  : primary
  | unaryOperator attributeInstance? primary
  | expression binaryOperator attributeInstance* expression
  | expression '?' attributeInstance* expression ':' expression
  ;

mintypmaxExpression
  : expression
  | expression ':' expression ':' expression
  ;

modulePathExpression
  : modulePathPrimary
  | unaryModulePathOperator attributeInstance* modulePathPrimary
  | modulePathExpression binaryModulePathOperator attributeInstance* modulePathExpression
  | modulePathExpression '?' attributeInstance* modulePathExpression ':' modulePathExpression
  ;

modulePathMintypmaxExpression
  : modulePathExpression
  | modulePathExpression ':' modulePathExpression ':' modulePathExpression
  ;

rangeExpression
  : expression
  | constantExpression ':' constantExpression
  | expression '+:' constantExpression
  | expression '-:' constantExpression
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.4 Primaries
////////////////////////////////////////////////////////////////////////////////

constantPrimary
  : number
  | IDENTIFIER ('[' constantRangeExpression ']')?
  | constantConcatenation
  | constantMultipleConcatenation
  | constantFunctionCall
  | constantSystemFunctionCall
  | '(' constantMintypmaxExpression ')'
  | STRING
  ;

modulePathPrimary
  : number
  | IDENTIFIER
  | modulePathConcatenation
  | modulePathMultipleConcatenation
  | functionCall
  | systemFunctionCall
  | '(' modulePathMintypmaxExpression ')'
  ;

primary
  : number
  | hierarchicalIdentifier (('[' expression ']')* '[' rangeExpression ']' )?
  | concatenation
  | multipleConcatenation
  | functionCall
  | systemFunctionCall
  | '(' mintypmaxExpression ')'
  | STRING
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.5 expression left-side values
////////////////////////////////////////////////////////////////////////////////

netLvalue
  :  hierarchicalIdentifier (('[' constantExpression ']')* '[' constantRangeExpression ']')?
  | '{' netLvalue (',' netLvalue)* '}'
  ;

variableLvalue
  :  hierarchicalIdentifier (('[' expression ']')* '[' rangeExpression ']')?
  | '{' variableLvalue (',' variableLvalue)* '}'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.6 Operators
////////////////////////////////////////////////////////////////////////////////

unaryOperator
  : '+'
  | '-'
  | '!'
  | '~'
  | '&'
  | '~&'
  | '|'
  | '~|'
  | '^'
  | '~^'
  | '^~'
  ;

binaryOperator
  : '+'
  | '-'
  | '*'
  | '/'
  | '%'
  | '=='
  | '!='
  | '==='
  | '!=='
  | '&&'
  | '||'
  | '**'
  | '<'
  | '<='
  | '>'
  | '>='
  | '&'
  | '|'
  | '^'
  | '^~'
  | '~^'
  | '>>'
  | '<<'
  | '>>>'
  | '<<<'
  ;

unaryModulePathOperator
  : '!'
  | '~'
  | '&'
  | '~&'
  | '|'
  | '~|'
  | '^'
  | '~^'
  | '^~'
  ;

binaryModulePathOperator
  : '=='
  | '!='
  | '&&'
  | '||'
  | '&'
  | '|'
  | '^'
  | '^~'
  | '~^'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.6 Numbers
////////////////////////////////////////////////////////////////////////////////

number
  : decimalNumber
  | OINT
  | BINT
  | HINT
  | realNumber
  ;

decimalNumber
  : DVALUE
  | DINT
  | DX
  | DZ
  ;

realNumber
  : REAL
  | REALEXP
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.8 Strings
////////////////////////////////////////////////////////////////////////////////

// Lexer

////////////////////////////////////////////////////////////////////////////////
// A.9.1 Attributes
////////////////////////////////////////////////////////////////////////////////

attributeInstance
  : '(' '*' attrSpec (',' attrSpec)* '*' ')'
  ;

attrSpec
  : IDENTIFIER ('=' constantExpression)?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.9.2 Comments
////////////////////////////////////////////////////////////////////////////////

// Lexer

////////////////////////////////////////////////////////////////////////////////
// A.9.3 Identifiers
////////////////////////////////////////////////////////////////////////////////

hierarchicalIdentifier
  : (IDENTIFIER ('[' constantExpression ']')? '.')* IDENTIFIER
  ;

listOfIdentifiers
  : IDENTIFIER (',' IDENTIFIER)*
  ;

////////////////////////////////////////////////////////////////////////////////
// 19. Compiler directives
////////////////////////////////////////////////////////////////////////////////

directive
  : defaultNettypeDirective
  | includeDirective
  | timescaleDirective
  | defineDirective
  | undefDirective
  ;

defaultNettypeDirective
  : '`default_nettype' (netType | IDENTIFIER)
  ;

includeDirective
  : '`include' STRING
  ;

timescaleDirective
  : '`timescale' DVALUE IDENTIFIER '/' DVALUE IDENTIFIER
  ;

defineDirective
  : '`define' IDENTIFIER constantExpression
  ;

undefDirective
  : '`undef' IDENTIFIER
  ;
