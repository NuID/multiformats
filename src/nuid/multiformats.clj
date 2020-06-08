(ns nuid.multiformats
  (:require
   [multiformats.varint :as varint]
   [multiformats.codec :as codec])
  (:import
   [java.io ByteArrayOutputStream]))

(def key->code
  (comp
   varint/encode
   codec/key->code))

(defn -concat
  [& bs]
  (with-open [out (ByteArrayOutputStream.)]
    (doseq [b bs]
      (.write out b 0 (count b)))
    (.toByteArray out)))

(defn prefixed
  [k bytes]
  (-concat (key->code k) bytes))

(defn unprefixed
  [bytes]
  (let [[_ offset] (varint/read-bytes bytes 0)
        length     (- (count bytes) offset)]
    (with-open [out (ByteArrayOutputStream.)]
      (.write out bytes offset length)
      (.toByteArray out))))
