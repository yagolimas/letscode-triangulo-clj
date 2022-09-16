(ns triangulo.core
  (:require [clojure.math :refer [pow to-degrees acos round sqrt]]))

(defn calc-perimetro
  "Calcula o perimetro do triangulo, dado A B e C"
  [a b c]
  (+ a b c))

(defn calc-radianos
  "TODO: Calcular radianos dado lados a b e c de um triangulo"
  [a b c]
  )

(defn calc-angulo
  "TODO: Calcula o ângulo ∠A, dado A B C."
  [a b c]
  (let [side-a-squared (pow a 2)
        side-b-squared (pow b 2)
        side-c-squared (pow c 2)
        a-cos (/ (- (+ side-b-squared side-c-squared) side-a-squared) (* 2 b c))]
    (to-degrees (acos a-cos))))

(defn calc-area
  "TODO: Calcula a área de um triângulo usando a formula de Heron."
  [a b c]
  (let [semi-perimeter (/ (calc-perimetro a b c) 2)]
    (-> semi-perimeter
        (* (- semi-perimeter a))
        (* (- semi-perimeter b))
        (* (- semi-perimeter c))
        sqrt)))

(defn calc-altura
  "TODO: Calcula altura de A, dado a AREA."
  [a area]
  (let [height (/ (* area 2) a)]
    height))

(defn equilateral?
  "TODO: Verifica se o triangulo é equilateral"
  [a b c]
  (= a b c))

(defn isosceles?
  "TODO: Verifica se pelo menos dois lados sao iguais."
  [a b c]
  (or (= a b) (= b c) (= c a)))

(defn escaleno?
  "TODO: Verifica se os lados dos triangulos sao diferentes entre si."
  [a b c]
  (and (not (equilateral? a b c)) (not (isosceles? a b c))))

(defn obter-angulos
  "Obter um vetor com os angulos dos lados a b c"
  [a b c]
  (let [cycle-fn (comp (partial take 3) (partial drop 1) cycle)
        sides-a [a b c]
        sides-b (cycle-fn sides-a)
        sides-c (cycle-fn sides-b)
        calc-angles (fn [sides] (apply calc-angulo sides))]
    [(calc-angles sides-a) (calc-angles sides-b) (calc-angles sides-c)]))

(defn retangulo?
  "TODO: Verifica se é um triangulo retangulo, cujos angulos são iguais a 90o.
  O resultado não é exato, dado que cada angulo é arredondado utilizando clojure.math/round."
  [a b c]
  (let [angles (obter-angulos a b c)
        equal-angle-90? #(= (round %) 90)]
    (boolean (some equal-angle-90? angles))))

(defn obtuso?
  "TODO: Verifica se o triangulo é obtuso, tendo algum angulo >90o."
  [a b c]
  (let [angles (obter-angulos a b c)
        angle-above-90? #(> (round %) 90)]
    (boolean (some angle-above-90? angles))))

(defn agudo?
  "TODO: Verifica se o triangulo é obtuso, tendo algum angulo <90o."
  [a b c]
  (let [angles (obter-angulos a b c)
        angles-below-90? #(< (round %) 90)]
    (every? angles-below-90? angles)))

(defn gerar-dados-completos
  [a b c]
  (let [area (calc-area a b c)]
    {:lados [a b c]
     :retagulo (retangulo? a b c)
     :obtuso (obtuso? a b c)
     :agudo (agudo? a b c)
     :escaleno (escaleno? a b c)
     :isosceles (isosceles? a b c)
     :equilateral (equilateral? a b c)
     :area area
     :altura [(calc-altura a area)
              (calc-altura b area)
              (calc-altura c area)]
     :angulos [(calc-angulo a b c)
               (calc-angulo b c a)
               (calc-angulo c a b)]}))

(comment
  (require 'clojure.pprint)
  (escaleno? 60 51.96152 30)
  (retangulo? 60 51.96152 30)
  (clojure.pprint/pprint (gerar-dados-completos 30 20 44))
  (clojure.pprint/pprint (gerar-dados-completos 60 51.96152 30))
  (clojure.pprint/pprint (gerar-dados-completos 15.14741 28.08887 30))
  )
