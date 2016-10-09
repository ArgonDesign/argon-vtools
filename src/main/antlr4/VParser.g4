parser grammar VParser;

options {
  tokenVocab = VLexer;
}

start
  : source_text EOF
  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.1 Library source text
////////////////////////////////////////////////////////////////////////////////

//library_text
//  : library_description*
//  ;
//
//library_description
//  : library_declaration
//  | include_statement
//  | config_declaration
//  ;
//
//library_declaration
//  : 'library' IDENTIFIER file_path_spec (',' file_path_spec)*
//     ('-' 'incdir' file_path_spec (',' file_path_spec)* )? ';'
//  ;
//
//include_statement
//  : 'include' file_path_spec ';'
//  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.2 Verilog source text
////////////////////////////////////////////////////////////////////////////////

source_text
  : directive*
    description*
    directive*
  ;

description
  : module_declaration
//| udp_declaration
  | config_declaration
  ;

module_declaration
  : attribute_instance*
    'module' IDENTIFIER module_parameter_port_list? list_of_ports ';'
      module_item*
    'endmodule'
  | attribute_instance*
    'module' IDENTIFIER module_parameter_port_list? list_of_port_declarations? ';'
      non_port_module_item*
    'endmodule'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.3 Module parameters and ports
////////////////////////////////////////////////////////////////////////////////

module_parameter_port_list
  : '#' '(' parameter_declaration (',' parameter_declaration)* ')'
  ;

list_of_ports
  : '(' port (',' port)* ')'
  ;

list_of_port_declarations
  : '(' port_declaration (',' port_declaration)* ')'
  | '(' ')'
  ;

port
  : port_expression?
  | '.' IDENTIFIER '(' port_expression? ')'
  ;

port_expression
  : port_reference
  | '{' port_reference (',' port_reference)* '}'
  ;

port_reference
  : IDENTIFIER ('[' constant_range_expression ']')?
  ;

port_declaration
  : attribute_instance* inout_declaration
  | attribute_instance* input_declaration
  | attribute_instance* output_declaration
  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.4 Module items
////////////////////////////////////////////////////////////////////////////////

module_item
  : port_declaration ';'
  | non_port_module_item
  ;

module_or_generate_item
  : attribute_instance* module_or_generate_item_declaration
  | attribute_instance* local_parameter_declaration ';'
  | attribute_instance* parameter_override
  | attribute_instance* continuous_assign
  | attribute_instance* gate_instantiation
//| attribute_instance* udp_instantiation
  | attribute_instance* module_instantiation
  | attribute_instance* initial_construct
  | attribute_instance* always_construct
  | attribute_instance* loop_generate_construct
  | attribute_instance* conditional_generate_construct
  ;

module_or_generate_item_declaration
  : net_declaration
  | reg_declaration
  | integer_declaration
  | real_declaration
  | time_declaration
  | realtime_declaration
  | event_declaration
  | genvar_declaration
  | task_declaration
  | function_declaration
  ;

non_port_module_item
  : module_or_generate_item
  | generate_region
  | specify_block
  | attribute_instance* parameter_declaration ';'
  | attribute_instance* specparam_declaration
  ;

parameter_override
  : 'defparam' list_of_defparam_assignments ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.1.5 Configuration source text
////////////////////////////////////////////////////////////////////////////////

config_declaration
  : 'config' IDENTIFIER ';'
      design_statement
      config_rule_statement*
    'endconfig'
  ;

design_statement
  :  'design' ((IDENTIFIER '.')? IDENTIFIER)* ';'
  ;

config_rule_statement
  : 'default' liblist_clause ';'
  | inst_clause liblist_clause ';'
  | inst_clause use_clause ';'
  | cell_clause liblist_clause ';'
  | cell_clause use_clause ';'
  ;

inst_clause
  : 'instance' inst_name
  ;

inst_name
  : IDENTIFIER ('.' IDENTIFIER)*
  ;

cell_clause
  : 'cell' (IDENTIFIER '.')? IDENTIFIER
  ;

liblist_clause
  : 'liblist' IDENTIFIER*
  ;

use_clause
  : 'use' (IDENTIFIER '.')? IDENTIFIER (':' 'config')?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.1.1 Module parameter declarations
////////////////////////////////////////////////////////////////////////////////

local_parameter_declaration
  : 'localparam' 'signed'? vrange? list_of_param_assignments
  | 'localparam' parameter_type list_of_param_assignments
  ;

parameter_declaration
  : 'parameter' 'signed'? vrange? list_of_param_assignments
  | 'parameter' parameter_type list_of_param_assignments
  ;

specparam_declaration
  : 'specparam' vrange? list_of_specparam_assignments ';'
  ;

parameter_type
  : 'integer'
  | 'real'
  | 'realtime'
  | 'time'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.1.1 Port declarations
////////////////////////////////////////////////////////////////////////////////

inout_declaration
  : 'inout' net_type? 'signed'? vrange? list_of_identifiers
  ;

input_declaration
  : 'input' net_type? 'signed'? vrange? list_of_identifiers
  ;

output_declaration
  : 'output' net_type? 'signed'? vrange? list_of_identifiers
  | 'output' 'reg' 'signed'? vrange? list_of_variable_port_identifiers
  | 'output' output_variable_type list_of_variable_port_identifiers
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.1.3 Type declarations
////////////////////////////////////////////////////////////////////////////////

event_declaration
  : 'event' list_of_event_identifiers ';'
  ;

integer_declaration
  : 'integer' list_of_variable_identifiers ';'
  ;

net_declaration
  : net_type
    'signed'? delay3? list_of_net_identifiers ';'
  | net_type drive_strength?
    'signed'? delay3? list_of_net_decl_assignments ';'
  | net_type ('vectored' | 'scalared')?
    'signed'? vrange delay3? list_of_net_identifiers ';'
  | net_type drive_strength? ('vectored' | 'scalared')?
    'signed'? vrange delay3? list_of_net_decl_assignments ';'
  | 'trireg' charge_strength?
    'signed'? delay3? list_of_net_identifiers ';'
  | 'trireg' drive_strength?
    'signed'? delay3? list_of_net_decl_assignments ';'
  | 'trireg' charge_strength? ('vectored' | 'scalared')?
    'signed'? vrange delay3? list_of_net_identifiers ';'
  | 'trireg' drive_strength? ('vectored' | 'scalared')?
    'signed' vrange delay3? list_of_net_decl_assignments ';'
  ;

real_declaration
  : 'real' list_of_real_identifiers ';'
  ;

realtime_declaration
  : 'realtime' list_of_real_identifiers ';'
  ;

reg_declaration
  : 'reg' 'signed'? vrange? list_of_variable_identifiers ';'
  ;

time_declaration
  : 'time' list_of_variable_identifiers ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.2.1 Net and variable types
////////////////////////////////////////////////////////////////////////////////

net_type
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

output_variable_type
  : 'integer'
  | 'time'
  ;

real_type
  : IDENTIFIER dimension*
  | IDENTIFIER '=' constant_expression
  ;

variable_type
  : IDENTIFIER dimension*
  | IDENTIFIER '=' constant_expression
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.2.2 Strengths
////////////////////////////////////////////////////////////////////////////////

drive_strength
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

charge_strength
  : '(' 'small' ')'
  | '(' 'medium' ')'
  | '(' 'large' ')'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.2.3 Delays
////////////////////////////////////////////////////////////////////////////////

delay3
  : '#' delay_value
  | '#' '(' mintypmax_expression (',' mintypmax_expression (',' mintypmax_expression)?)? ')'
  ;

delay2
  : '#' delay_value
  | '#' '(' mintypmax_expression (',' mintypmax_expression)? ')'
  ;

delay_value
  : DVALUE
//| real_number
  | IDENTIFIER
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.3 Declaration lists
////////////////////////////////////////////////////////////////////////////////

list_of_defparam_assignments
  : defparam_assignment (',' defparam_assignment)*
  ;

list_of_event_identifiers
  : IDENTIFIER dimension* (',' IDENTIFIER dimension*)*
  ;

list_of_net_decl_assignments
  : net_decl_assignment (',' net_decl_assignment )*
  ;

list_of_net_identifiers
  : IDENTIFIER dimension* (',' IDENTIFIER dimension*)*
  ;

list_of_param_assignments
  : param_assignment (',' param_assignment)*
  ;

list_of_real_identifiers
  : real_type (',' real_type)*
  ;

list_of_specparam_assignments
  : specparam_assignment (',' specparam_assignment)*
  ;

list_of_variable_identifiers
  : variable_type (',' variable_type)*
  ;

list_of_variable_port_identifiers
  : IDENTIFIER ('=' constant_expression)? (',' IDENTIFIER ('=' constant_expression)?)*
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.4 Declaration assignmets
////////////////////////////////////////////////////////////////////////////////

defparam_assignment
  : hierarchical_identifier '=' constant_mintypmax_expression
  ;

net_decl_assignment
  : IDENTIFIER '=' expression
  ;

param_assignment
  : IDENTIFIER '=' constant_mintypmax_expression
  ;

specparam_assignment
  : IDENTIFIER '=' constant_mintypmax_expression
  | pulse_control_specparam
  ;

pulse_control_specparam
  : 'PATHPULSE' '$' '=' '(' constant_mintypmax_expression (',' constant_mintypmax_expression)? ')'
  | 'PATHPULSE' '$' specify_terminal_descriptor '$'
    specify_terminal_descriptor '=' '(' constant_mintypmax_expression (',' constant_mintypmax_expression)? ')'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.5 Declaration ranges
////////////////////////////////////////////////////////////////////////////////

dimension
  : '[' constant_expression ':' constant_expression ']'
  ;

vrange
  : '[' constant_expression ':' constant_expression ']'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.6 Function declarations
////////////////////////////////////////////////////////////////////////////////

function_declaration
  : 'function' 'automatic'? function_range_or_type? IDENTIFIER ';'
      function_item_declaration+
      statement
    'endfunction'
  | 'function' 'automatic'? function_range_or_type? IDENTIFIER '(' function_port_list ')' ';'
      block_item_declaration*
      statement
    'endfunction'
  ;

function_item_declaration
  : block_item_declaration
  | attribute_instance* tf_input_declaration ';'
  ;

function_port_list
  : attribute_instance* tf_input_declaration (',' attribute_instance* tf_input_declaration)*
  ;

function_range_or_type
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

task_declaration
  : 'task' 'automatic'? IDENTIFIER ';'
      task_item_declaration*
      statement_or_null
    'endtask'
  | 'task' 'automatic'? IDENTIFIER '(' task_port_list? ')' ';'
      block_item_declaration*
      statement_or_null
    'endtask'
  ;
task_item_declaration
  : block_item_declaration
  | attribute_instance* tf_input_declaration ';'
  | attribute_instance* tf_output_declaration ';'
  | attribute_instance* tf_inout_declaration ';'
  ;

task_port_list
  : task_port_item (',' task_port_item)*
  ;

task_port_item
  : attribute_instance* tf_input_declaration
  | attribute_instance* tf_output_declaration
  ;

tf_input_declaration
  : 'input' 'reg'? 'signed'? vrange? list_of_identifiers
  | 'input' task_port_type list_of_identifiers
  ;

tf_output_declaration
  : 'output' 'reg'? 'signed'? vrange? list_of_identifiers
  | 'output' task_port_type list_of_identifiers
  ;

tf_inout_declaration
  : 'inout' 'reg'? 'signed'? vrange? list_of_identifiers
  | 'inout' task_port_type list_of_identifiers
  ;

task_port_type
  : 'integer'
  | 'real'
  | 'realtime'
  | 'time'
  | attribute_instance* tf_inout_declaration
  ;

////////////////////////////////////////////////////////////////////////////////
// A.2.8 Block item declarations
////////////////////////////////////////////////////////////////////////////////

block_item_declaration
  : attribute_instance* 'reg' 'signed'? vrange? list_of_block_variable_identifiers ';'
  | attribute_instance* 'integer' list_of_block_variable_identifiers ';'
  | attribute_instance* 'time' list_of_block_variable_identifiers ';'
  | attribute_instance* 'real' list_of_block_real_identifiers ';'
  | attribute_instance* 'realtime' list_of_block_real_identifiers ';'
  | attribute_instance* event_declaration
  | attribute_instance* local_parameter_declaration ';'
  | attribute_instance* parameter_declaration ';'
  ;

list_of_block_variable_identifiers
  : block_variable_type (',' block_variable_type)*
  ;

list_of_block_real_identifiers
  : block_real_type (',' block_real_type)*
  ;

block_variable_type
  : IDENTIFIER dimension*
  ;

block_real_type
  : IDENTIFIER dimension*
  ;

////////////////////////////////////////////////////////////////////////////////
// A.3.1 Primitive instantiation and instances
////////////////////////////////////////////////////////////////////////////////

gate_instantiation
  : cmos_switchtype delay3? cmos_switch_instance (',' cmos_switch_instance)* ';'
  | enable_gatetype drive_strength? delay3? enable_gate_instance (',' enable_gate_instance)* ';'
  | mos_switchtype delay3? mos_switch_instance (',' mos_switch_instance)* ';'
  | n_input_gatetype drive_strength? delay2? n_input_gate_instance (',' n_input_gate_instance)* ';'
  | n_output_gatetype drive_strength? delay2? n_output_gate_instance (',' n_output_gate_instance)* ';'
  | pass_en_switchtype delay2? pass_enable_switch_instance (',' pass_enable_switch_instance)* ';'
  | pass_switchtype pass_switch_instance (',' pass_switch_instance)* ';'
  | 'pulldown' pulldown_strength? pull_gate_instance (',' pull_gate_instance)* ';'
  | 'pullup' pullup_strength? pull_gate_instance (',' pull_gate_instance)* ';'
  ;

cmos_switch_instance
  : name_of_gate_instance?
    '(' net_lvalue ',' expression ',' expression ',' expression ')'
  ;

enable_gate_instance
  : name_of_gate_instance?
    '(' net_lvalue ',' expression ',' expression ')'
  ;

mos_switch_instance
  : name_of_gate_instance?
    '(' net_lvalue ',' expression ',' expression ')'
  ;

n_input_gate_instance
  : name_of_gate_instance?
    '(' net_lvalue ',' expression (',' expression)* ')'
  ;

n_output_gate_instance
  : name_of_gate_instance?
    '(' net_lvalue (',' net_lvalue)* ',' expression ')'
  ;

pass_switch_instance
  : name_of_gate_instance?
    '(' net_lvalue ',' net_lvalue ')' ';'
  ;

pass_enable_switch_instance
  : name_of_gate_instance?
    '(' net_lvalue ',' net_lvalue ',' expression ')'
  ;

pull_gate_instance
  : name_of_gate_instance?
    '(' net_lvalue ')'
  ;

name_of_gate_instance
  : IDENTIFIER vrange?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.3.2 Primitive strengths
////////////////////////////////////////////////////////////////////////////////

pulldown_strength
  : '(' strength0 ',' strength1 ')'
  | '(' strength1 ',' strength0 ')'
  | '(' strength0 ')'
  ;

pullup_strength
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

cmos_switchtype
  : 'cmos'
  | 'rcmos'
  ;

enable_gatetype
  : 'bufif0'
  | 'bufif1'
  | 'notif0'
  | 'notif1'
  ;

mos_switchtype
  : 'nmos'
  | 'pmos'
  | 'rnmos'
  | 'rpmos'
  ;

n_input_gatetype
  : 'and'
  | 'nand'
  | 'or'
  | 'nor'
  | 'xor'
  | 'xnor'
  ;

n_output_gatetype
  : 'buf'
  | 'not'
  ;

pass_en_switchtype
  : 'tranif0'
  | 'tranif1'
  | 'rtranif1'
  | 'rtranif0'
  ;

pass_switchtype
  : 'tran'
  | 'rtran'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.4.1 Module instantiation
////////////////////////////////////////////////////////////////////////////////

module_instantiation
  : IDENTIFIER parameter_value_assignment? module_instance (',' module_instance)* ';'
  ;

parameter_value_assignment
  : '#' '(' list_of_parameter_assignments ')'
  ;

list_of_parameter_assignments
  : ordered_parameter_assignment (',' ordered_parameter_assignment)*
  | named_parameter_assignment (',' named_parameter_assignment)*
  ;

ordered_parameter_assignment
  : expression
  ;

named_parameter_assignment
  : '.' IDENTIFIER '(' mintypmax_expression? ')'
  ;

module_instance
  : name_of_module_instance '(' list_of_port_connections ')'
  ;

name_of_module_instance
  : IDENTIFIER vrange?
  ;

list_of_port_connections
  : ordered_port_connection? (',' ordered_port_connection?)*
  | named_port_connection (',' named_port_connection)*
  ;

ordered_port_connection
  : attribute_instance* expression
  | attribute_instance+
  ;

named_port_connection
  : attribute_instance* '.' IDENTIFIER '(' expression? ')'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.4.2 Generate construct
////////////////////////////////////////////////////////////////////////////////

generate_region
  : 'generate' module_or_generate_item* 'endgenerate'
  ;

genvar_declaration
  : 'genvar' list_of_identifiers ';'
  ;

loop_generate_construct
  : 'for' '(' genvar_initialization ';' genvar_expression ';' genvar_iteration ')'
    generate_block
  ;

genvar_initialization
  : IDENTIFIER '=' constant_expression
  ;

genvar_expression
  : genvar_primary
  | unary_operator attribute_instance* genvar_primary
  | genvar_expression binary_operator attribute_instance* genvar_expression
  | genvar_expression '?' attribute_instance* genvar_expression ':' genvar_expression
  ;

genvar_iteration
  : IDENTIFIER '=' genvar_expression
  ;

genvar_primary
  : constant_primary
  | IDENTIFIER
  ;

conditional_generate_construct
  : if_generate_construct
  | case_generate_construct
  ;

if_generate_construct
  : 'if' '(' constant_expression ')' generate_block_or_null ('else' generate_block_or_null)?
  ;

case_generate_construct
  : 'case' '(' constant_expression ')'
      case_generate_item+
    'endcase'
  ;

case_generate_item
  : constant_expression (',' constant_expression)* ':' generate_block_or_null
  | 'default' ':'? generate_block_or_null
  ;

generate_block
  : module_or_generate_item
  | 'begin' (':' IDENTIFIER)?  module_or_generate_item* 'end'
  ;

generate_block_or_null
  : generate_block
  | ';'
  ;

//////////////////////////////////////////////////////////////////////////////////
//// A.5.1 UDP declaration
//////////////////////////////////////////////////////////////////////////////////
//
//udp_declaration
//  : attribute_instance*
//    'primitive' IDENTIFIER '(' udp_port_list ')' ';'
//      udp_port_declaration+
//      udp_body
//    'endprimitive'
//  | attribute_instance*
//    'primitive' IDENTIFIER '(' udp_declaration_port_list ')' ';'
//      udp_body
//    'endprimitive '
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.5.2 UDP ports
//////////////////////////////////////////////////////////////////////////////////
//
//udp_port_list
//  : IDENTIFIER ',' IDENTIFIER (',' IDENTIFIER)*
//  ;
//
//udp_declaration_port_list
//  : udp_output_declaration ',' udp_input_declaration (',' udp_input_declaration)*
//  ;
//
//udp_port_declaration
//  : udp_output_declaration ';'
//  | udp_input_declaration ';'
//  | udp_reg_declaration ';'
//  ;
//
//udp_output_declaration
//  : attribute_instance* output IDENTIFIER
//  | attribute_instance* output reg IDENTIFIER ('=' constant_expression)?
//  ;
//
//udp_input_declaration
//  : attribute_instance* input list_of_identifiers
//  ;
//
//udp_reg_declaration
//  : attribute_instance* reg IDENTIFIER
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.5.3 UDP body
//////////////////////////////////////////////////////////////////////////////////
//
//udp_body
//  : combinational_body
//  | sequential_body
//  ;
//
//combinational_body
//  : 'table' combinational_entry+ 'endtable'
//  ;
//
//combinational_entry
//  : level_input_list ':' output_symbol ';'
//  ;
//
//sequential_body
//  : udp_initial_statement? 'table' sequential_entry+ 'endtable'
//  ;
//
//udp_initial_statement
//  : 'initial' IDENTIFIER '=' init_val ';'
//  ;
//
//init_val
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
//sequential_entry
//  : seq_input_list ':' level_symbol ':' next_state ';'
//  ;
//
//seq_input_list
//  : level_input_list
//  | edge_input_list
//  ;
//
//level_input_list
//  : level_symbol+
//  ;
//
//edge_input_list
//  : level_symbol* edge_indicator level_symbol*
//  ;
//
//edge_indicator
//  : '(' level_symbol level_symbol ')'
//  | edge_symbol
//  ;
//
//next_state
//  : output_symbol
//  | '-'
//  ;
//
//output_symbol
//  : '0'
//  | '1'
//  | 'x'
//  | 'X'
//  ;
//
//level_symbol
//  : '0'
//  | '1'
//  | 'x'
//  | 'X'
//  | '?'
//  | 'b'
//  | 'B'
//  ;
//
//edge_symbol
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
//udp_instantiation
//  : IDENTIFIER drive_strength? delay2? udp_instance (',' udp_instance)* ';'
//  ;
//
//udp_instance
//  : name_of_udp_instance? '(' net_lvalue ',' expression (',' expression)* ')'
//  ;
//
//name_of_udp_instance
//  : IDENTIFIER vrange?
//  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.1 Continuous assignment startements
////////////////////////////////////////////////////////////////////////////////

continuous_assign
  : 'assign' drive_strength? delay3? list_of_net_assignments ';'
  ;

list_of_net_assignments
  : net_assignment (',' net_assignment)*
  ;

net_assignment
  : net_lvalue '=' expression
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.2 Procedural blocks and assignments
////////////////////////////////////////////////////////////////////////////////

initial_construct
  : 'initial' statement
  ;

always_construct
  : 'always' statement
  ;

blocking_assignment
  : variable_lvalue '=' delay_or_event_control? expression
  ;

nonblocking_assignment
  : variable_lvalue '<=' delay_or_event_control? expression
  ;

procedural_continuous_assignments
  : 'assign' variable_assignment
  | 'deassign' variable_lvalue
  | 'force' variable_assignment
  | 'force' net_assignment
  | 'release' variable_lvalue
  | 'release' net_lvalue
  ;

variable_assignment
  : variable_lvalue '=' expression
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.3 Parallel and sequential blocks
////////////////////////////////////////////////////////////////////////////////

par_block
  : 'fork' (':' IDENTIFIER block_item_declaration* )?
      statement*
    'join'
  ;

seq_block
  : 'begin' (':' IDENTIFIER  block_item_declaration*)?
      statement*
    'end'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.4 Statements
////////////////////////////////////////////////////////////////////////////////

statement
  : attribute_instance* blocking_assignment ';'
  | attribute_instance* case_statement
  | attribute_instance* conditional_statement
  | attribute_instance* disable_statement
  | attribute_instance* event_trigger
  | attribute_instance* loop_statement
  | attribute_instance* nonblocking_assignment ';'
  | attribute_instance* par_block
  | attribute_instance* procedural_continuous_assignments ';'
  | attribute_instance* procedural_timing_control_statement
  | attribute_instance* seq_block
  | attribute_instance* system_task_enable
  | attribute_instance* task_enable
  | attribute_instance* wait_statement
  ;

statement_or_null
  : statement
  | attribute_instance* ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.5 Timing control statemetns
////////////////////////////////////////////////////////////////////////////////

delay_control
  : '#' delay_value
  | '#' '(' mintypmax_expression ')'
  ;

delay_or_event_control
  : delay_control
  | event_control
  | 'repeat' '(' expression ')' event_control
  ;

disable_statement
  : 'disable' hierarchical_identifier ';'
  ;

event_control
  : '@' hierarchical_identifier
  | '@' '(' event_expression ')'
  | '@' '*'
  | '@' '(' '*' ')'
  ;

event_trigger
  : '->' hierarchical_identifier ('[' expression ']')* ';'
  ;

event_expression
  : expression
  | 'posedge' expression
  | 'negedge' expression
  | event_expression 'or' event_expression
  | event_expression ',' event_expression
  ;

procedural_timing_control
  : delay_control
  | event_control
  ;

procedural_timing_control_statement
  : procedural_timing_control statement_or_null
  ;

wait_statement
  : 'wait' '(' expression ')' statement_or_null
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.6 Conditional statements
////////////////////////////////////////////////////////////////////////////////

conditional_statement
  : 'if' '(' expression ')' statement_or_null ('else' statement_or_null)?
  | if_else_if_statement
  ;

if_else_if_statement
  : 'if' '(' expression ')' statement_or_null
    ('else' 'if' '(' expression ')' statement_or_null )*
    ('else' statement_or_null)?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.7 Case statements
////////////////////////////////////////////////////////////////////////////////

case_statement
  : 'case' '(' expression ')'
      case_item+
    'endcase'
  | 'casez' '(' expression ')'
      case_item+
    'endcase'
  | 'casex' '(' expression ')'
      case_item+
    'endcase'
  ;

case_item
  : expression (',' expression)* ':' statement_or_null
  | 'default' ':'? statement_or_null
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.8 looping statements
////////////////////////////////////////////////////////////////////////////////

loop_statement
  : 'forever' statement
  | 'repeat' '(' expression ')' statement
  | 'while' '(' expression ')' statement
  | 'for' '(' variable_assignment ';' expression ';' variable_assignment ')' statement
  ;

////////////////////////////////////////////////////////////////////////////////
// A.6.9 Task enable statemetns
////////////////////////////////////////////////////////////////////////////////

system_task_enable
  : SYSID ('(' expression? (',' expression?)* ')')? ';'
  ;

task_enable
  : hierarchical_identifier ('(' expression (',' expression)* ')')? ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.7.1 Specify block declaration
////////////////////////////////////////////////////////////////////////////////

specify_block
  : 'specify' specify_item* 'endspecify'
  ;

specify_item
  : specparam_declaration
  | pulsestyle_declaration
  | showcancelled_declaration
  | path_declaration
//| system_timing_check
  ;

pulsestyle_declaration
  : 'pulsestyle_onevent' list_of_paths ';'
  | 'pulsestyle_ondetect' list_of_paths ';'
  ;

showcancelled_declaration
  : 'showcancelled' list_of_paths ';'
  | 'noshowcancelled' list_of_paths ';'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.7.2 Specify path declarations
////////////////////////////////////////////////////////////////////////////////

path_declaration
  : simple_path_declaration ';'
  | edge_sensitive_path_declaration ';'
  | state_dependent_path_declaration ';'
  ;

simple_path_declaration
  : parallel_path_description '=' path_delay_value
  | full_path_description '=' path_delay_value
  ;

parallel_path_description
  : '(' specify_terminal_descriptor polarity_operator? '=>' specify_terminal_descriptor ')'
  ;

full_path_description
  : '(' list_of_paths polarity_operator? '*>' list_of_paths ')'
  ;

list_of_paths
  : specify_terminal_descriptor (',' specify_terminal_descriptor)*
  ;

////////////////////////////////////////////////////////////////////////////////
// A.7.3 Specify block terminals
////////////////////////////////////////////////////////////////////////////////

specify_terminal_descriptor
  : IDENTIFIER ('[' constant_range_expression ']')?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.7.4 Specify path delays
////////////////////////////////////////////////////////////////////////////////

path_delay_value
  : list_of_path_delay_expressions
  | '(' list_of_path_delay_expressions ')'
  ;

list_of_path_delay_expressions
  : constant_mintypmax_expression (',' constant_mintypmax_expression)*
  ;

edge_sensitive_path_declaration
  : parallel_edge_sensitive_path_description '=' path_delay_value
  | full_edge_sensitive_path_description '=' path_delay_value
  ;

parallel_edge_sensitive_path_description
  : '(' edge_identifier? specify_terminal_descriptor '=>'
    '(' specify_terminal_descriptor polarity_operator? ':' expression ')' ')'
  ;

full_edge_sensitive_path_description
  : '(' edge_identifier? list_of_paths '*>'
    '(' list_of_paths polarity_operator? ':' expression ')' ')'
  ;

edge_identifier
  : 'posedge'
  | 'negedge'
  ;

state_dependent_path_declaration
  : 'if' '(' module_path_expression ')' simple_path_declaration
  | 'if' '(' module_path_expression ')' edge_sensitive_path_declaration
  | 'ifnone' simple_path_declaration
  ;

polarity_operator
  : '+'
  | '-'
  ;

//////////////////////////////////////////////////////////////////////////////////
//// A.7.5.1 System timing check comands
//////////////////////////////////////////////////////////////////////////////////
//
//system_timing_check
//  : $setup_timing_check
//  | $hold_timing_check
//  | $setuphold_timing_check
//  | $recovery_timing_check
//  | $removal_timing_check
//  | $recrem_timing_check
//  | $skew_timing_check
//  | $timeskew_timing_check
//  | $fullskew_timing_check
//  | $period_timing_check
//  | $width_timing_check
//  | $nochange_timing_check
//  ;
//
//$setup_timing_check
//  : '$setup' '(' timing_check_event ',' timing_check_event ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$hold_timing_check
//  : '$hold' '(' timing_check_event ',' timing_check_event ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$setuphold_timing_check
//  : '$setuphold' '(' timing_check_event ',' timing_check_event ',' expression ',' expression
//    (',' IDENTIFIER? (',' mintypmax_expression? (',' mintypmax_expression? (',' delayed_reference? (',' delayed_data?)?)?)?)?)? ')' ';'
//  ;
//
//$recovery_timing_check
//  : '$recovery' '(' timing_check_event ',' timing_check_event ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$removal_timing_check
//  : '$removal' '(' timing_check_event ',' timing_check_event ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$recrem_timing_check
//  : '$recrem' '(' timing_check_event ',' timing_check_event ',' expression ',' expression
//    (',' IDENTIFIER? (',' mintypmax_expression? (',' mintypmax_expression? (',' delayed_reference? (',' delayed_data?)?)?)?)?)? ')' ';'
//  ;
//
//$skew_timing_check
//  : '$skew' '(' timing_check_event ',' timing_check_event ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$timeskew_timing_check
//  : '$timeskew' '(' timing_check_event ',' timing_check_event ',' expression
//    (',' IDENTIFIER? (',' constant_expression? (',' constant_expression?)?)?)? ')' ';'
//  ;
//
//$fullskew_timing_check
//  : '$fullskew' '(' timing_check_event ',' timing_check_event ',' expression ',' expression
//    (',' IDENTIFIER? (',' constant_expression? (',' constant_expression?)?)?)? ')' ';'
//  ;
//
//$period_timing_check
//  : '$period' '(' controlled_timing_check_event ',' expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//$width_timing_check
//  : '$width' '(' controlled_timing_check_event ',' expression
//    (',' constant_expression (',' IDENTIFIER)?)? ')' ';'
//  ;
//
//$nochange_timing_check
//  : '$nochange' '(' timing_check_event ',' timing_check_event ',' mintypmax_expression ','
//    mintypmax_expression (',' IDENTIFIER?)? ')' ';'
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.7.5.2 system timing check command arguments
//////////////////////////////////////////////////////////////////////////////////
//
//delayed_data
//  : IDENTIFIER constant_mintypmax_expression?
//  ;
//
//delayed_reference
//  : IDENTIFIER constant_mintypmax_expression?
//  ;
//
//////////////////////////////////////////////////////////////////////////////////
//// A.7.5.3 System timing check event definitions
//////////////////////////////////////////////////////////////////////////////////
//
//timing_check_event
//  : timing_check_event_control? specify_terminal_descriptor ('&&&' timing_check_condition)?
//  ;
//
//controlled_timing_check_event
//  : timing_check_event_control specify_terminal_descriptor ('&&&' timing_check_condition)?
//  ;
//
//timing_check_event_control
//  : 'posedge'
//  | 'negedge'
//  | edge_control_specifier
//  ;
//
//edge_control_specifier
//  : 'edge' (edge_descriptor (',' edge_descriptor)*)?
//  ;
//
//edge_descriptor2
//  : '01'
//  | '10'
//  | z_or_x zero_or_one
//  | zero_or_one z_or_x
//  ;
//
//zero_or_one
//  : '0'
//  | '1'
//  ;
//
//z_or_x
//  : 'x'
//  | 'X'
//  | 'z'
//  | 'Z'
//  ;
//
//timing_check_condition
//  : scalar_timing_check_condition
//  | '(' scalar_timing_check_condition ')'
//  ;
//
//scalar_timing_check_condition
//  : expression
//  | '~' expression
//  | expression '==' scalar_constant
//  | expression '===' scalar_constant
//  | expression '!=' scalar_constant
//  | expression '!==' scalar_constant
//  ;
//
//scalar_constant
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

constant_concatenation
  : '{' constant_expression (',' constant_expression)* '}'
  ;

constant_multiple_concatenation
  : '{' constant_expression constant_concatenation '}'
  ;

module_path_concatenation
  : '{' module_path_expression (',' module_path_expression)* '}'
  ;

module_path_multiple_concatenation
  : '{' constant_expression module_path_concatenation '}'
  ;

multiple_concatenation
  : '{' constant_expression concatenation '}'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.2 Function calls
////////////////////////////////////////////////////////////////////////////////

constant_function_call
  : IDENTIFIER attribute_instance* '(' constant_expression (',' constant_expression)* ')'
  ;

constant_system_function_call
  : SYSID '(' constant_expression (',' constant_expression)* ')'
  ;

function_call
  : hierarchical_identifier attribute_instance* '(' expression (',' expression)* ')'
  ;

system_function_call
  : SYSID ('(' expression (',' expression)* ')')?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.3 Expressions
////////////////////////////////////////////////////////////////////////////////

constant_expression
  : constant_primary
  | unary_operator attribute_instance* constant_primary
  | constant_expression binary_operator attribute_instance* constant_expression
  | constant_expression '?' attribute_instance* constant_expression ':' constant_expression
  ;

constant_mintypmax_expression
  : constant_expression
  | constant_expression ':' constant_expression ':' constant_expression
  ;

constant_range_expression
  : constant_expression
  | constant_expression ':' constant_expression
  | constant_expression '+:' constant_expression
  | constant_expression '-:' constant_expression
  ;

expression
  : primary
  | unary_operator attribute_instance? primary
  | expression binary_operator attribute_instance* expression
  | expression '?' attribute_instance* expression ':' expression
  ;

mintypmax_expression
  : expression
  | expression ':' expression ':' expression
  ;

module_path_expression
  : module_path_primary
  | unary_module_path_operator attribute_instance* module_path_primary
  | module_path_expression binary_module_path_operator attribute_instance* module_path_expression
  | module_path_expression '?' attribute_instance* module_path_expression ':' module_path_expression
  ;

module_path_mintypmax_expression
  : module_path_expression
  | module_path_expression ':' module_path_expression ':' module_path_expression
  ;

range_expression
  : expression
  | constant_expression ':' constant_expression
  | expression '+:' constant_expression
  | expression '-:' constant_expression
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.4 Primaries
////////////////////////////////////////////////////////////////////////////////

constant_primary
  : number
  | IDENTIFIER ('[' constant_range_expression ']')?
  | constant_concatenation
  | constant_multiple_concatenation
  | constant_function_call
  | constant_system_function_call
  | '(' constant_mintypmax_expression ')'
  | STRING
  ;

module_path_primary
  : number
  | IDENTIFIER
  | module_path_concatenation
  | module_path_multiple_concatenation
  | function_call
  | system_function_call
  | '(' module_path_mintypmax_expression ')'
  ;

primary
  : number
  | hierarchical_identifier (('[' expression ']')* '[' range_expression ']' )?
  | concatenation
  | multiple_concatenation
  | function_call
  | system_function_call
  | '(' mintypmax_expression ')'
  | STRING
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.5 expression left-side values
////////////////////////////////////////////////////////////////////////////////

net_lvalue
  :  hierarchical_identifier (('[' constant_expression ']')* '[' constant_range_expression ']')?
  | '{' net_lvalue (',' net_lvalue)* '}'
  ;

variable_lvalue
  :  hierarchical_identifier (('[' expression ']')* '[' range_expression ']')?
  | '{' variable_lvalue (',' variable_lvalue)* '}'
  ;

////////////////////////////////////////////////////////////////////////////////
// A.8.6 Operators
////////////////////////////////////////////////////////////////////////////////

unary_operator
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

binary_operator
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

unary_module_path_operator
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

binary_module_path_operator
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
  : decimal_number
  | OINT
  | BINT
  | HINT
  | real_number
  ;

decimal_number
  : DVALUE
  | DINT
  | DX
  | DZ
  ;

real_number
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

attribute_instance
  : '(*' attr_spec (',' attr_spec)* '*)'
  ;

attr_spec
  : IDENTIFIER ('=' constant_expression)?
  ;

////////////////////////////////////////////////////////////////////////////////
// A.9.2 Comments
////////////////////////////////////////////////////////////////////////////////

// Lexer

////////////////////////////////////////////////////////////////////////////////
// A.9.3 Identifiers
////////////////////////////////////////////////////////////////////////////////

hierarchical_identifier
  : (IDENTIFIER ('[' constant_expression ']')? '.')* IDENTIFIER
  ;

list_of_identifiers
  : IDENTIFIER (',' IDENTIFIER)*
  ;

////////////////////////////////////////////////////////////////////////////////
// 19. Compiler directives
////////////////////////////////////////////////////////////////////////////////

directive
  : default_nettype_directive
  | include_directive
  | timescale_directive
  | define_directive
  | undef_directive
  ;

default_nettype_directive
  : '`default_nettype' (net_type | IDENTIFIER)
  ;

include_directive
  : '`include' STRING
  ;

timescale_directive
  : '`timescale' DVALUE IDENTIFIER '/' DVALUE IDENTIFIER
  ;

define_directive
  : '`define' IDENTIFIER constant_expression
  ;

undef_directive
  : '`undef' IDENTIFIER
  ;
