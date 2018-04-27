////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Alint
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.alint.warnings

import com.argondesign.alint.Visitor
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.Source
import com.argondesign.alint.SourceWarning

class WarningsSourceAnalyserVisitor[T <: SourceWarning]
    extends Visitor[List[T]](Nil, (_ ::: _))
    with SourceAnalyser[List[T]] {
}
