/*
Copyright 2019 Naoki Matsumoto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scala.util.Random

class MemUnitSpec extends ChiselFlatSpec {
  implicit val conf = RV16KConfig()
  assert(Driver(() => new MemUnit) {
    c =>
      new PeekPokeTester(c) {
        poke(c.io.memRead, false)
        poke(c.io.memWrite, false)
        for (i <- 0 until 100) {
          val v = Random.nextInt(0xFFFF)
          poke(c.io.in, v.U(16.W))
          step(1)
          expect(c.io.out, v.U(16.W))
        }

        var testDataArray: Array[UInt] = Array.empty
        poke(c.io.memWrite, true)
        for(i <- 0 until 100){
          val v = Random.nextInt(0xFFFF)
          poke(c.io.address, i.U(9.W))
          poke(c.io.in, v.U(16.W))
          testDataArray = testDataArray :+ v.U(16.W)
          step(1)
        }
        poke(c.io.memWrite, false)
        poke(c.io.memRead, true)
        for(i <- 0 until 100){
          poke(c.io.address, i.U(9.W))
          expect(c.io.out, testDataArray(i))
        }
      }
  })
}
