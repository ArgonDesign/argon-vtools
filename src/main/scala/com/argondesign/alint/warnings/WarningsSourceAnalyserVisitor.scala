////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Argon Verilog Tools
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.vtools.warnings

import com.argondesign.vtools.Visitor
import com.argondesign.vtools.SourceAnalyser
import com.argondesign.vtools.Source
import com.argondesign.vtools.SourceWarning

class WarningsSourceAnalyserVisitor[T <: SourceWarning]
    extends Visitor[List[T]](Nil, (_ ::: _))
    with SourceAnalyser[List[T]] {
}
