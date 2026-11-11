# Taller para la Casa – Antipatrones
## La Oficina de Visas del Caos (Java)

**Universidad Distrital Francisco José de Caldas – Ingeniería de Sistemas**


**Integrante:**
- Sharon Stefany Castilla Araque – 20251020089



---

## 1. Parte 1 – Cacería de antipatrones

| Fragmento | Antipatrón | Síntoma que lo delata | Consecuencia en el consulado |
|---|---|---|---|
| 1. `OficinaDeTodo` (la ventanilla de Ramiro) | **God Object** | Una sola clase y un solo método validan el pasaporte, cobran, guardan el expediente y mandan el correo. | Si falla o cambia una tarea hay que tocar toda la clase, y si Ramiro se enferma el consulado entero se detiene. No se puede probar ni reemplazar una tarea por separado. |
| 2. `CalculadoraDeTarifas` (la tarifa clonada) | **Copy-Paste Programming** | Las líneas `50 * meses` y `total + 10` están copiadas idénticas en `visaTurista`, `visaEstudiante` y `visaTrabajo`. | Al subir la tarifa base se corrigieron dos copias y la tercera quedó con el precio viejo: se cobra distinto por el mismo error. Cada cambio cuesta 3 ediciones y el riesgo de olvidar una crece con cada copia. |
| 3. `EvaluadorMagico` (los números misteriosos) | **Magic Numbers** | Aparecen `tipo == 3`, `edad > 17`, `edad < 66`, `0.5`, `1.5` sin ningún nombre que diga qué significan. | Nadie sabe con certeza qué es el 3 y nadie se atreve a cambiarlo. Un tipo inexistente (como el 9) se rechaza en silencio, sin explicar por qué. |
| 4. `SolicitudAcoplada` (la cancelación en cadena) | **Acoplamiento fuerte** | La solicitud hace `new` de `CorreoServicio`, `CitasServicio` y `PagosServicio` y los llama uno por uno dentro de `cancelar()`. | Un viernes se olvidaron de llamar a la caja y a un viajero se le perdió el reembolso. Agregar un aviso nuevo obliga a editar la solicitud, y no se puede probar sin los tres servicios reales. |

---

## 2. Parte 2 – El rescate (estructura del proyecto)

```
src/
├── legado/              código heredado (solo para comparar en consola)
├── responsabilidades/   ValidadorPasaporte, CobradorDeTarifa,
│                        RepositorioDeExpedientes, NotificadorPorCorreo, TramiteVisa
├── tarifas/             ConfiguracionTarifas, EstrategiaTarifa (Strategy),
│                        TarifaTurista, TarifaEstudiante, TarifaTrabajo, CalculadoraTarifas
├── visas/               TipoVisa (enum), ReglasVisa (constantes), ResultadoVisa, EvaluadorVisa,
│                        TipoVisaInvalidoException
├── observer/            Solicitud (sujeto), ObservadorSolicitud, EstadoSolicitud,
│                        CorreoObservador, CitasObservador, PagosObservador
└── app/                 Main con los 5 casos de prueba
```

| Antipatrón | Solución aplicada |
|---|---|
| God Object | Responsabilidad única: una clase por tarea. `TramiteVisa` solo coordina el orden de los pasos, no hace ninguno. |
| Copy-Paste | Patrón Strategy. La cuenta base (`precioBaseMes * meses + CARGO_FIJO`) vive una sola vez en `CalculadoraTarifas`; cada estrategia solo aplica su factor. El precio base está únicamente en `ConfiguracionTarifas`. |
| Magic Numbers | `enum TipoVisa` con código, rango de edad y tarifa; constantes con nombre (`ReglasVisa.EDAD_MINIMA_ADULTO`, `LONGITUD_MINIMA`, `FACTOR`, `DESCUENTO_ESTUDIANTE`, `RECARGO_TRABAJO`); `EstadoSolicitud` y `ResultadoVisa` en vez de textos sueltos. Un tipo inválido lanza `TipoVisaInvalidoException` con mensaje claro; la lista de tipos válidos se arma desde `TipoVisa.values()`, así que una visa nueva no obliga a tocar la excepción. |
| Acoplamiento fuerte | Patrón Observer. `Solicitud` solo conoce la interfaz `ObservadorSolicitud`; correo, citas y pagos se suscriben desde afuera. |

**Nota sobre el fragmento 4:** en el código heredado `cancelar()` no llama a `PagosServicio` (así lo dice el enunciado: se olvidaron de la caja). `PagosServicio` sí tiene `reembolsar()`, pero nadie lo invoca.

**Nota sobre el fragmento 2:** en el código original el `50` estaba escrito a mano dentro de cada método. Para poder medir en consola "cuántos lugares hay que editar", en la versión heredada de este proyecto cada copia tiene su propio campo con un setter que cuenta la edición. La lógica clonada es la misma.

---

## 3. Parte 3 – Casos de prueba (salida en consola)

Se genera con `java -cp out app.Main`. La salida completa está también en `salida_consola.txt`.

```
==================================================
CASO 1 - God Object
==================================================
--- Version heredada (Ramiro lo hace todo) ---
[OficinaDeTodo/Ramiro] Cobrando $190.0 a Ana Torres
[OficinaDeTodo/Ramiro] Guardando expediente de Ana Torres
[OficinaDeTodo/Ramiro] Correo para Ana Torres: su visa va en camino
Resultado: tramite aprobado, pero todo salio de UNA sola clase

--- Version rescatada (una clase por responsabilidad) ---
[ValidadorPasaporte] pasaporte valido
[CobradorDeTarifa] cobrando $190.0 a Ana Torres
[RepositorioDeExpedientes] guardando expediente de Ana Torres
[NotificadorPorCorreo] correo para Ana Torres: su visa va en camino
Resultado: tramite aprobado, cada tarea la hizo una clase distinta

--- Version rescatada con pasaporte invalido ---
[ValidadorPasaporte] pasaporte invalido
[TramiteVisa] pasaporte invalido, tramite detenido antes de cobrar
Resultado: tramite rechazado, no se cobro ni se guardo nada

==================================================
CASO 2 - Copy-Paste Programming
==================================================
--- Version clonada: subir la base de 50 a 60 ---
Lugares editados: 3
--- Version Strategy: subir la base de 50 a 60 ---
Lugares editados: 1

Precios para 3 meses (clonada | Strategy):
  Turista    : 190.0 | 190.0
  Estudiante : 95.0 | 95.0
  Trabajo    : 285.0 | 285.0

--- Y si en la clonada se olvidan de una copia (como paso en el consulado)? ---
  Trabajo (clonada con olvido): 240.0  <-- precio viejo, deberia ser 285.0

==================================================
CASO 3 - Magic Numbers
==================================================
--- Tipo 2 con edad 20 ---
Heredada (tipo == 2)           : APROBADA
Rescatada (TipoVisa.ESTUDIANTE): APROBADA

--- Tipo 9 con edad 20 ---
Heredada: RECHAZADA  (rechazada en silencio, nadie sabe por que)
Rescatada: ERROR CLARO -> El tipo de visa 9 no existe. Tipos validos: TURISTA(1), ESTUDIANTE(2), TRABAJO(3)

==================================================
CASO 4 - Acoplamiento fuerte
==================================================
--- Version heredada ---
   [Correo] aviso de cancelacion enviado
   [Citas] cupo liberado en la agenda
Estado: CANCELADA
Reembolso procesado: false  <-- se olvidaron de la caja, el viajero pierde su plata

--- Version con Observer ---
   [Correo] aviso de cancelacion enviado a Ana Torres
   [Citas] cupo liberado de Ana Torres
   [Pagos] reembolso de $190.0 procesado para Ana Torres
Estado: CANCELADA
Reembolsos procesados: 1  <-- la solicitud nunca conocio a los servicios

==================================================
CASO 5 - Integracion (viaje completo, version nueva)
==================================================
1) Solicitar
   Solicitud de visa TRABAJO creada para Luis Perez (estado ACTIVA)
2) Evaluar
   Edad 25 -> APROBADA
3) Cobrar
[CobradorDeTarifa] cobrando $465.0 a Luis Perez
4) Cancelar
   [Correo] aviso de cancelacion enviado a Luis Perez
   [Citas] cupo liberado de Luis Perez
   [Pagos] reembolso de $465.0 procesado para Luis Perez
   Estado final: CANCELADA | reembolsos: 1

TABLA FINAL: antipatrones capturados
Antipatron             | Sintoma                            | Solucion
-----------------------+------------------------------------+------------------------------
God Object             | Ramiro valida, cobra, guarda y     | Responsabilidad unica (1 clase
                       | manda el correo                    | por tarea) + TramiteVisa
Copy-Paste             | Misma cuenta copiada 3 veces       | Strategy + base en un solo lugar
Magic Numbers          | tipo == 3, * 0.5, edad > 17        | enum TipoVisa y constantes
Acoplamiento fuerte    | La solicitud crea y llama a los    | Observer (la solicitud solo
                       | 3 servicios a mano                 | conoce una interfaz)
```

Resumen de resultados:

| Caso | Resultado |
|---|---|
| 1 God Object | Ambas versiones aprueban; en la nueva se ve qué clase hizo cada tarea. Con pasaporte inválido, la nueva se detiene antes de cobrar. |
| 2 Copy-Paste | Clonada: 3 lugares editados. Strategy: 1. Los precios finales coinciden (190 / 95 / 285 para 3 meses). Si se olvida una copia, la clonada cobra 240 en vez de 285. |
| 3 Magic Numbers | Tipo 2 y `ESTUDIANTE` dan lo mismo (APROBADA). El tipo 9 se rechaza en silencio en la heredada y con mensaje claro en la nueva. |
| 4 Acoplamiento | La heredada deja el reembolso en `false`; la nueva avisa a correo, citas y pagos. |
| 5 Integración | Solicitar → evaluar → cobrar → cancelar funciona completo y se imprime la tabla final. |

---

## 4. Cómo evita nuestra implementación volver a caer en los antipatrones

Cada tarea del trámite vive en su propia clase y `TramiteVisa` solo decide el orden, así que ya no existe un "nuevo Ramiro": si una pieza falla o cambia se reemplaza sola. El precio base está en un único lugar (`ConfiguracionTarifas`) y la cuenta se hace una sola vez en `CalculadoraTarifas`, por lo que subir la tarifa es un cambio de una línea y no se pueden desincronizar copias. Los números y textos sueltos se reemplazaron por el enum `TipoVisa`, constantes con nombre y estados tipados, y un tipo de visa inexistente falla con un mensaje claro en vez de rechazarse en silencio; una visa nueva es una constante más en el enum, sus constantes en `ReglasVisa` y su clase de tarifa, sin tocar el resto. Por último, `Solicitud` solo conoce la interfaz `ObservadorSolicitud`, así que agregar un aviso nuevo es crear un observador y suscribirlo, sin editar la solicitud y sin que nadie tenga que acordarse de llamar a mano a cada servicio.

---

## 5. Cómo compilar y ejecutar

Desde la raíz del repositorio, en PowerShell:

```powershell
cd TallerAntipatrones
mkdir out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out app.Main
```
