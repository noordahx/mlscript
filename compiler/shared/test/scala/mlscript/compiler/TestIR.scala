package mlscript
package compiler

import mlscript.utils.shorthands._
import mlscript.compiler.ir._
import scala.collection.mutable.StringBuilder
import mlscript.compiler.optimizer.TailRecOpt

import IRDiffTestCompiler.*

class IRDiffTestCompiler extends DiffTests(State) {

  override def postProcess(mode: ModeType, basePath: List[Str], testName: Str, unit: TypingUnit, output: Str => Unit, raise: Diagnostic => Unit): (List[Str], Option[TypingUnit]) = 
    val outputBuilder = StringBuilder()
    if (mode.useIR || mode.irVerbose)
      try
        val fnUid = FreshInt()
        val classUid = FreshInt()
        val tag = FreshInt()

        val gb = Builder(Fresh(), fnUid, classUid, tag, raise)
        val graph_ = gb.buildGraph(unit)
        
        if !mode.noTailRecOpt then 
          output("\nIR:")
          output(graph_.toString())

        val graph = 
          if !mode.noTailRecOpt then
            val tailRecOpt = new TailRecOpt(fnUid, classUid, tag, raise)
            val (g, comps) = tailRecOpt.run_debug(graph_)
            output("\nStrongly Connected Tail Calls:")
            output(comps.toString)
            g
          else
            graph_

        if !mode.noTailRecOpt then
          output(graph.toString())
        
        output("\nPromoted:")
        output(graph.toString())
        var interp_result: Opt[Str] = None
        if (mode.interpIR)
          output("\nInterpreted:")
          val ir = Interpreter(mode.irVerbose).interpret(graph)
          interp_result = Some(ir)
          output(ir)

      catch
        case err: Exception =>
          output(s"\nIR Processing Failed: ${err.getMessage()}")
          if (mode.irVerbose) then
            output("\n" ++ err.getStackTrace().map(_.toString()).mkString("\n"))
        case err: StackOverflowError =>
          output(s"\nIR Processing Failed: ${err.getMessage()}")
          if (mode.irVerbose) then
            output("\n" ++ err.getStackTrace().map(_.toString()).mkString("\n"))
      
    (outputBuilder.toString().linesIterator.toList, None)
  
}

object IRDiffTestCompiler {
  
  lazy val State =
    new DiffTests.State(DiffTests.pwd/"compiler"/"shared"/"test"/"diff-ir")
  
}